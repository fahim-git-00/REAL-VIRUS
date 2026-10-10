package main

import (
    "fmt"
    "net"
    "time"
)

func scan() {
    for i := 1; i < 65535; i++ {
        addr := fmt.Sprintf("127.0.0.1:%d", i)
        c, err := net.DialTimeout("tcp", addr, 50*time.Millisecond)
        if err == nil {
            fmt.Println("OPEN", addr)
            c.Close()
        }
    }
}
