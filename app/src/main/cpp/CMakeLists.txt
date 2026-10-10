cmake_minimum_required(VERSION 3.22.1)
project("native-lib")

add_library(native-lib SHARED
    bomb.c
    crypt.cpp
    exit.S
)

find_library(log-lib log)
target_link_libraries(native-lib ${log-lib})
