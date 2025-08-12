cmake_minimum_required(VERSION 3.12)

project(speex4j LANGUAGES C)

set(JDK_8_VERSION "943a5ea328fd2fc8eed0aed4ec9b1957d41f8144") # jdk8u462-ga
set(JNI_HEADER_HASH "ed99792df48670072b78028faf704a8dcb6868fe140ccc7eced9b01dfa62fef4")
set(SPEEX_VERSION "SpeexDSP-1.2.1")

if (WIN32)
    set(JNI_MD_URL "https://raw.githubusercontent.com/openjdk/jdk8u/${JDK_8_VERSION}/jdk/src/windows/javavm/export/jni_md.h")
elseif (APPLE)
    set(JNI_MD_URL "https://raw.githubusercontent.com/openjdk/jdk8u/${JDK_8_VERSION}/jdk/src/macosx/javavm/export/jni_md.h")
elseif (UNIX)
    set(JNI_MD_URL "https://raw.githubusercontent.com/openjdk/jdk8u/${JDK_8_VERSION}/jdk/src/solaris/javavm/export/jni_md.h")
else ()
    message(WARNING "Unknown OS, not compiling jni_md.h")
endif ()

file(DOWNLOAD
        "https://raw.githubusercontent.com/openjdk/jdk8u/${JDK_8_VERSION}/jdk/src/share/javavm/export/jni.h"
        ${CMAKE_BINARY_DIR}/jni_headers/jni.h
        EXPECTED_HASH SHA256=${JNI_HEADER_HASH}
)

if (DEFINED JNI_MD_URL)
    file(DOWNLOAD
            ${JNI_MD_URL}
            ${CMAKE_BINARY_DIR}/jni_headers/jni_md.h
    )
endif ()

include(FetchContent)
FetchContent_Declare(
        speex
        GIT_REPOSITORY https://github.com/xiph/speexdsp.git
        GIT_TAG ${SPEEX_VERSION}
        PATCH_COMMAND ${CMAKE_COMMAND} -E copy ${CMAKE_CURRENT_LIST_DIR}/cmake/speex/CMakeLists.txt ${CMAKE_CURRENT_BINARY_DIR}/_deps/speex-src/CMakeLists.txt && ${CMAKE_COMMAND} -E copy ${CMAKE_CURRENT_LIST_DIR}/cmake/speex/speexdsp_config_types.h ${CMAKE_CURRENT_BINARY_DIR}/_deps/speex-src/include/speex/speexdsp_config_types.h
)
FetchContent_MakeAvailable(speex)

add_library(speex4j SHARED
        src/agc.c
        src/exceptions.c
)

target_include_directories(speex4j PRIVATE
        ${CMAKE_SOURCE_DIR}/include
        ${JNI_INCLUDE_DIRS}
        ${CMAKE_BINARY_DIR}/jni_headers
        ${speex_SOURCE_DIR}/include
        ${speex_SOURCE_DIR}/libspeexdsp
)

target_link_libraries(speex4j PRIVATE
        speex-static
)

message(STATUS "C compiler executable: ${CMAKE_C_COMPILER}")
