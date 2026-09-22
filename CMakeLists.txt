cmake_minimum_required(VERSION 3.18.1)
project("skbaudiodsp")

add_library(
    audio_dsp_jni
    SHARED
    jni/audio_dsp_jni.cpp
)

find_library(
    log-lib
    log
)

target_link_libraries(
    audio_dsp_jni
    ${log-lib}
)
