.name "OoOoh"
.comment "I can do anything!"
mode 4
nop nop nop nop nop nop nop nop nop nop
nop nop nop nop nop nop nop nop nop nop
nop nop nop nop nop nop nop nop nop nop
nop nop nop nop nop nop nop nop nop
ll r0, 0x0125
lc r2, 0xfd
ll r3, 0xfe7e
str [r2], r3
fork
bz r0
ll r0, 0x007a
b r0
nop nop nop nop nop nop nop nop nop nop
nop nop nop nop nop nop nop nop nop nop
nop nop nop nop nop nop nop nop nop nop
nop nop nop nop nop
ll r0, 0xfe7a
or r1, r0
lc r3, 0xfd
ll r5, 0xf1cf
ll r6, 0xf47f
mov r7, r6
mov r8, r6
mov r9, r6
mov r10, r6
mov r11, r6
mov r12, r6
mov r13, r6
mov r14, r6
mov r15, r6
ldb [r0], 0x00, 0x44
mov r0, r0
mov r0, r0
nop nop nop nop nop
ll r2, 0x0077
mode 8
b r2
mode 3
ll r0, 0xfe77
or r1, r0
ll r2, 0xfe7d
ll r3, 0xfe7e
ll r4, 0xe42e
ll r5, 0x7ff3
ll r6, 0x7f53
ll r7, 0x7f63
ll r8, 0x7f73
ll r9, 0x7f83
ll r10, 0x7f93
ll r11, 0x7fa3
ll r12, 0x7fb3
ll r13, 0x7fc3
ll r14, 0x7fd3
ll r15, 0x7fe3
nop nop
mode 8
str [r2], r4
str [r3], r5
b r1
nop nop nop nop nop nop nop nop nop nop
nop nop nop nop nop nop nop nop nop nop
nop nop nop nop nop nop nop nop nop nop
nop nop nop nop nop nop
ll r0, 0x0025
lc r2, 0xfd
ll r3, 0xfe7e
str [r2], r3
fork
bz r0
ll r0, 0x00b4
b r0
nop nop nop nop nop nop nop nop nop nop
nop nop nop nop nop nop nop nop nop nop
nop nop nop nop nop nop nop
ll r0, 0x0025
lc r2, 0xfd
ll r3, 0xfe7e
str [r2], r3
fork
bz r0
ll r0, 0xfea3
b r0
nop nop nop nop nop nop nop nop nop nop
nop nop nop nop nop nop nop nop nop nop
nop nop nop nop nop nop nop
ll r0, 0x0125
lc r2, 0xfd
ll r3, 0xfe7e
str [r2], r3
fork
bz r0
ll r0, 0x00ae
b r0
nop nop nop nop nop nop nop nop nop nop
nop nop nop nop nop nop nop nop nop nop
nop nop nop nop nop nop nop nop nop nop
nop nop nop nop nop nop nop nop nop nop
nop nop nop nop nop nop nop nop nop
b r4
mode 3
ll r0, 0x0177
ll r2, 0x017d
ll r3, 0x017e
ll r4, 0xe42e
ll r5, 0x7f63
ll r6, 0x7f73
ll r7, 0x7f83
ll r8, 0x7f93
ll r9, 0x7fa3
ll r10, 0x7fb3
ll r11, 0x7fc3
ll r12, 0x7fd3
ll r13, 0x7fe3
ll r14, 0x7ff3
ll r15, 0x7f53
stat r1, 0x0
ll r1, 0x00fd
b r1
ll r0, 0x017a
lc r3, 0xfd
ll r5, 0xf47f
mov r6, r5
mov r7, r5
mov r8, r5
mov r9, r5
mov r10, r5
mov r11, r5
mov r13, r5
mov r12, r5
mov r14, r5
ll r15, 0xf1cf
ldb [r0], 0x00, 0x3d
nop nop nop nop nop nop
ll r2, 0x00b4
mode 8
b r2
nop nop nop nop nop nop nop nop nop nop
nop nop nop nop nop nop nop nop nop nop
nop nop nop nop nop nop nop nop nop nop
ll r0, 0x0081
lc r2, 0xfd
ll r3, 0xfe7e
str [r2], r3
fork
bz r0
mode 3
ll r0, 0x017a
ll r1, 0x00fa
ll r4, 0x017d
ldb [r0], 0x00, 0xd1
write r0
nop nop
ll r5, 0x004a
mode 6
b r5
nop nop nop nop nop nop nop nop nop nop
nop nop nop nop nop nop nop nop nop nop
nop nop nop nop nop nop nop nop nop nop
nop nop nop nop nop nop nop nop nop nop
nop nop nop nop nop nop nop nop nop nop
nop nop nop nop nop nop nop nop nop nop
nop nop nop nop nop nop nop nop
mode 8
str [r2], r4
str [r3], r11
b r0
ldb [r1], 0x00, 0x01
write r1
ll r0, 0x794b
ll r15, 0x03e8
rol r14, 0x0
mode 11
mov r14, r14
stb [r15], 0x00, 0x0a
stb [r0], 0x00, 0x01
