package main

import (
    "fmt"
    "bufio"
    "log"
    "os"
)

func main() {
    urls := []string{}
    file, err := os.Open("urls.txt")
    if err != nil {
        log.Fatal(err)
    }
    defer file.Close()

    scanner := bufio.NewScanner(file)
    for scanner.Scan() {
        line := scanner.Text()
        urls = append(urls, line)
    }

    if err := scanner.Err(); err != nil {
        log.Fatal(err)
    }
    for _, u := range urls {
        fmt.Println(u)
    }
}

