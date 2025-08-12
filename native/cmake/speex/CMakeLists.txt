cmake_minimum_required(VERSION 3.15)
project(speex LANGUAGES C)

set(CMAKE_C_STANDARD 99)
set(CMAKE_C_STANDARD_REQUIRED ON)

file(GLOB SPEEX_SRC CONFIGURE_DEPENDS
        "${CMAKE_CURRENT_SOURCE_DIR}/libspeexdsp/*.c"
)

add_library(speex-static STATIC ${SPEEX_SRC})
set_target_properties(speex-static PROPERTIES
        OUTPUT_NAME speex
        POSITION_INDEPENDENT_CODE ON
)

target_include_directories(speex-static PUBLIC
        ${CMAKE_CURRENT_SOURCE_DIR}
        ${CMAKE_CURRENT_SOURCE_DIR}/include
        ${CMAKE_CURRENT_SOURCE_DIR}/libspeexdsp
)

target_compile_definitions(speex-static PRIVATE EXPORT=)

target_compile_definitions(speex-static PRIVATE FLOATING_POINT)
target_compile_definitions(speex-static PRIVATE USE_SMALLFT)
