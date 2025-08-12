#include <jni.h>
#include <math.h>
#include <stdbool.h>
#include <stdlib.h>
#include <stdint.h>
#include <speex/speex_preprocess.h>

#include "exceptions.h"

typedef struct AgcState {
    SpeexPreprocessState *state;
    int32_t frame_size;
    spx_int16_t *buf;
} AgcState;

/**
 * Gets the state from the AutomaticGainControl java object.
 *
 * @param env the JNI environment
 * @param pointer the pointer to the state
 * @return the state or NULL - If the state could not be retrieved, this will throw a runtime exception in Java
 */
AgcState *get_state(JNIEnv *env, const jlong pointer) {
    if (pointer == 0) {
        throw_runtime_exception(env, "AGC is closed");
        return NULL;
    }
    return (AgcState *) (uintptr_t) pointer;
}

JNIEXPORT jlong JNICALL Java_de_maxhenkel_speex4j_AutomaticGainControl_createAgc0(
    JNIEnv *env,
    jclass clazz,
    const jint frame_size,
    const jint sample_rate
) {
    SpeexPreprocessState *speex_preprocess_state = speex_preprocess_state_init(frame_size, sample_rate);

    int agc = 1;
    int result = 0;
    result = speex_preprocess_ctl(speex_preprocess_state, SPEEX_PREPROCESS_SET_AGC, &agc);

    if (result < 0) {
        throw_io_exception(env, "Failed to enable AGC");
        return 0;
    }

    spx_int16_t *buf = malloc(frame_size * sizeof(spx_int16_t));

    AgcState *agc_state = malloc(sizeof(AgcState));
    agc_state->state = speex_preprocess_state;
    agc_state->frame_size = frame_size;
    agc_state->buf = buf;
    return (jlong) (uintptr_t) agc_state;
}

JNIEXPORT void JNICALL Java_de_maxhenkel_speex4j_AutomaticGainControl_setTarget0(
    JNIEnv *env,
    jclass clazz,
    const jlong pointer,
    const jint target
) {
    const AgcState *state = get_state(env, pointer);
    if (state == NULL) {
        return;
    }
    if (target <= 0 || target > 32768) {
        throw_illegal_argument_exception(env, "Invalid target (must be 1..32768)");
        return;
    }
    spx_int32_t agc_target = target;
    const int result = speex_preprocess_ctl(state->state, SPEEX_PREPROCESS_SET_AGC_TARGET, &agc_target);
    if (result < 0) {
        throw_runtime_exception(env, "Failed to set target");
        return;
    }
}

JNIEXPORT jint JNICALL Java_de_maxhenkel_speex4j_AutomaticGainControl_getTarget0(
    JNIEnv *env,
    jclass clazz,
    const jlong pointer
) {
    const AgcState *state = get_state(env, pointer);
    if (state == NULL) {
        return 0;
    }
    spx_int32_t agc_target = -1;
    const int result = speex_preprocess_ctl(state->state, SPEEX_PREPROCESS_GET_AGC_TARGET, &agc_target);
    if (result < 0) {
        throw_runtime_exception(env, "Failed to get target");
        return 0;
    }
    return agc_target;
}

JNIEXPORT void JNICALL Java_de_maxhenkel_speex4j_AutomaticGainControl_setMaxGain0(
    JNIEnv *env,
    jclass clazz,
    const jlong pointer,
    const jint max_gain
) {
    const AgcState *state = get_state(env, pointer);
    if (state == NULL) {
        return;
    }
    if (max_gain < 0) {
        throw_illegal_argument_exception(env, "Invalid max gain (must be >= 0)");
        return;
    }
    spx_int32_t agc_max_gain = max_gain;
    const int result = speex_preprocess_ctl(state->state, SPEEX_PREPROCESS_SET_AGC_MAX_GAIN, &agc_max_gain);
    if (result < 0) {
        throw_runtime_exception(env, "Failed to set max gain");
        return;
    }
}

JNIEXPORT jint JNICALL Java_de_maxhenkel_speex4j_AutomaticGainControl_getMaxGain0(
    JNIEnv *env,
    jclass clazz,
    const jlong pointer
) {
    const AgcState *state = get_state(env, pointer);
    if (state == NULL) {
        return 0;
    }
    spx_int32_t max_gain = -1;
    const int result = speex_preprocess_ctl(state->state, SPEEX_PREPROCESS_GET_AGC_MAX_GAIN, &max_gain);
    if (result < 0) {
        throw_runtime_exception(env, "Failed to get max gain");
        return 0;
    }
    return max_gain;
}

JNIEXPORT void JNICALL Java_de_maxhenkel_speex4j_AutomaticGainControl_setIncrement0(
    JNIEnv *env,
    jclass clazz,
    const jlong pointer,
    const jint increment
) {
    const AgcState *state = get_state(env, pointer);
    if (state == NULL) {
        return;
    }
    if (increment < 0) {
        throw_illegal_argument_exception(env, "Invalid increment (must be >= 0)");
        return;
    }
    spx_int32_t agc_increment = increment;
    const int result = speex_preprocess_ctl(state->state, SPEEX_PREPROCESS_SET_AGC_INCREMENT, &agc_increment);
    if (result < 0) {
        throw_runtime_exception(env, "Failed to set increment");
        return;
    }
}

JNIEXPORT jint JNICALL Java_de_maxhenkel_speex4j_AutomaticGainControl_getIncrement0(
    JNIEnv *env,
    jclass clazz,
    const jlong pointer
) {
    const AgcState *state = get_state(env, pointer);
    if (state == NULL) {
        return 0;
    }
    spx_int32_t increment = -1;
    const int result = speex_preprocess_ctl(state->state, SPEEX_PREPROCESS_GET_AGC_INCREMENT, &increment);
    if (result < 0) {
        throw_runtime_exception(env, "Failed to get increment");
        return 0;
    }
    return increment;
}

JNIEXPORT void JNICALL Java_de_maxhenkel_speex4j_AutomaticGainControl_setDecrement0(
    JNIEnv *env,
    jclass clazz,
    const jlong pointer,
    const jint decrement
) {
    const AgcState *state = get_state(env, pointer);
    if (state == NULL) {
        return;
    }
    if (decrement > 0) {
        throw_illegal_argument_exception(env, "Invalid decrement (must be <= 0)");
        return;
    }
    spx_int32_t agc_decrement = decrement;
    const int result = speex_preprocess_ctl(state->state, SPEEX_PREPROCESS_SET_AGC_DECREMENT, &agc_decrement);
    if (result < 0) {
        throw_runtime_exception(env, "Failed to set decrement");
        return;
    }
}

JNIEXPORT jint JNICALL Java_de_maxhenkel_speex4j_AutomaticGainControl_getDecrement0(
    JNIEnv *env,
    jclass clazz,
    const jlong pointer
) {
    const AgcState *state = get_state(env, pointer);
    if (state == NULL) {
        return 0;
    }
    spx_int32_t decrement = -1;
    const int result = speex_preprocess_ctl(state->state, SPEEX_PREPROCESS_GET_AGC_DECREMENT, &decrement);
    if (result < 0) {
        throw_runtime_exception(env, "Failed to get decrement");
        return 0;
    }
    return decrement;
}

JNIEXPORT jboolean JNICALL Java_de_maxhenkel_speex4j_AutomaticGainControl_agc0(
    JNIEnv *env,
    jclass clazz,
    const jlong pointer,
    const jshortArray sample_input
) {
    const AgcState *state = get_state(env, pointer);
    if (state == NULL) {
        return false;
    }

    if (sample_input == NULL) {
        throw_illegal_argument_exception(env, "Input array is null");
        return false;
    }

    const jsize input_length = (*env)->GetArrayLength(env, sample_input);

    if (input_length != state->frame_size) {
        throw_illegal_argument_exception(
            env, string_format("Invalid input array length %d, expected %d", input_length, state->frame_size));
        return false;
    }

    (*env)->GetShortArrayRegion(env, sample_input, 0, state->frame_size, (jshort *) state->buf);
    const int result = speex_preprocess_run(state->state, state->buf);
    (*env)->SetShortArrayRegion(env, sample_input, 0, state->frame_size, (jshort *) state->buf);
    return (jboolean) result;
}

JNIEXPORT void JNICALL Java_de_maxhenkel_speex4j_AutomaticGainControl_destroyAgc0(
    JNIEnv *env,
    jobject obj,
    const jlong pointer
) {
    if (pointer == 0) {
        return;
    }
    AgcState *state = (AgcState *) (uintptr_t) pointer;
    speex_preprocess_state_destroy(state->state);
    free(state->buf);
    free(state);
}
