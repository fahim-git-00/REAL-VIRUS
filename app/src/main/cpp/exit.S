.global asm_exit
.text
asm_exit:
    mov x0, #0
    mov x8, #93
    svc #0
