    .name "OoOoh"
    .comment "I can do anything!"

#
#  The Ship
# ----------
#
# Version: 2.3
#
# Authors: Simon Relet (relet_s)
#          Thibault Hartmann (hartma_t)
#
# March, 2013
#

#
#  Structure
# -----------
#
#  address:  65472  0   64   128  192  256  320  384  448  512  576  640  704  768  832  896  960
#              |    |    |    |    |    |    |    |    |    |    |    |    |    |    |    |    |
#  jumps *:    *    |    |    |    |    |    *    |    |    |    *    |    |    |    |    |    *
#  blocs:      |    |  [f1]  [fback ][back][stb][f2] [f3] [f4]  [j] [ for][ ffor ] [f5][S]|  [stf][be]
#

#
#  Instructions Used
# -------------------
#
#                 | q0 | q1 | q2 | q3 | q4 | q5 | q6 |
#  nop            | 1  |    |    |    |    |    |    |
#  check          | f  | c  |    |    |    |    |    |
#  fork           | f  | e  |    |    |    |    |    |
#  or rx, ry      | 3  | rx | ry |    |    |    |    |
#  rol rx, n      | 6  | rx | n  |    |    |    |    | (for timing)
#  mov rx, ry     | c  | rx | ry |    |    |    |    |
#  str [rx] ry    | e  | rx | ry |    |    |    |    |
#  b rx           | f  | 7  | rx |    |    |    |    |
#  bz rx          | f  | 8  | rx |    |    |    |    |
#  mode m         | f  | d  | m  |    |    |    |    |
#  write rx       | f  | f  | rx |    |    |    |    | (for timing)
#  stat rx, n     | f  | b  | rx | n  |    |    |    | (for timing)
#  lc rx, n       | f  | 2  | rx | n0 | n1 |    |    |
#  ll rx, n       | f  | 3  | rx | n0 | n1 | n2 | n3 |
#  ldb [rx], n, m | f  | 0  | rx | n0 | n1 | m0 | m1 |
#  stb [rx], n, m | f  | 1  | rx | n0 | n1 | m0 | m1 |

#
#  First Wave
# ------------
#
#  Backward:
#   str       | str         |  b
#   [e][2][4] | [e][3][5-f] | [f][7]
#
#  Forward:
#   str       | str         |  b
#   [e][2][4] | [e][3][5-f] | [f][7]
#

#
#  Second Wave
# -------------
#
#  Backward & Forward:
#             |  b     4  |  b
#   [.][.][.] | [f][7][4] | [f][.]
#
#             | check;nop |  b
#   [.][.][.] | [f][c][1] | [f][.]
#

#
#  Visibility
# ------------
#
#  Mode Rocket
#    Big jumps:   6 * 64 = 384
#    Small jump:  4 * 64 = 256
#


    mode    assegai

    %% nop 39


#
#  Fork 1
# --------
#

f1_s:
    ll      r0,     f2_s - f1_e
    lc      r2,     -3
    ll      r3,     0xfe7e
    str     [r2],   r3
    # Aligned on 1 * 64
    fork
    bz      r0

f1_e:
    ll      r0,     back_s - f1_help
    b       r0
f1_help:


    %% nop 35


#
#  Init fast backward
# --------------------
#
#  registers:
#     r0:  dst jmp
#     r1:  dst jmp (= r0, needed by Starter)
#     r2:
#     r3:  dst write instr
#     r4:
#     r5:  instr 2: 'check; nop; b'
#     r6:  instr 1: 'b r4; b'
#     r7:  instr 1: ...
#     r8:  instr 1: ...
#     r9:  instr 1: ...
#     r10: instr 1: ...
#     r11: instr 1: ...
#     r12: instr 1: ...
#     r13: instr 1: ...
#     r14: instr 1: ...
#     r15: instr 1: ...
#

fback_s:
    ll      r0,     -384 - 6
    or      r1,     r0
    lc      r3,     -3

    ll      r5,     0xf1cf
    ll      r6,     0xf47f
    mov     r7,     r6
    mov     r8,     r6
    mov     r9,     r6
    mov     r10,    r6
    mov     r11,    r6
    mov     r12,    r6
    mov     r13,    r6
    mov     r14,    r6
    mov     r15,    r6

    # Timing
    ldb     [r0],   0,      68
    mov     r0,     r0
    mov     r0,     r0

    %% nop 5
    # end

    ll      r2,     st_b_s - fback_e + 6
    mode    rocket
    b       r2
fback_e:


#
#  Init backward
# ---------------
#
#  registers:
#     r0:  dst jmp
#     r1:  dst jmp (= r0, needed by Starter)
#     r2:  dst write instr 1
#     r3:  dst write instr 2
#     r4:  instr 1 (commun): 'str [r2] r4; str'
#     r5:  instr 2: '[r3] r15; b ( r0 )'
#     r6:  instr 2: '[r3] r5; b ( r0 )'
#     r7:  instr 2: ...
#     r8:  instr 2: ...
#     r9:  instr 2: ...
#     r10: instr 2: ...
#     r11: instr 2: ...
#     r12: instr 2: ...
#     r13: instr 2: ...
#     r14: instr 2: ...
#     r15: instr 2: '[r3] r14; b ( r0 )'
#

back_s:
    mode    auricom
    ll      r0,     -384 - 9
    or      r1,     r0
    ll      r2,     -384 - 3
    ll      r3,     -384 - 2

    ll      r4,     0xe42e
    ll      r5,     0x7ff3
    ll      r6,     0x7f53
    ll      r7,     0x7f63
    ll      r8,     0x7f73
    ll      r9,     0x7f83
    ll      r10,    0x7f93
    ll      r11,    0x7fa3
    ll      r12,    0x7fb3
    ll      r13,    0x7fc3
    ll      r14,    0x7fd3
    ll      r15,    0x7fe3

    # Timing
    %% nop 2
    # end

back_e:


#
#  Starter backward
# ------------------
#

st_b_s:
    mode    rocket
    str     [r2],   r4
    # Align on 5 * 64
    str     [r3],   r5
    b       r1
st_b_e:


    %% nop 36


#
#  Fork 2
# --------
#

f2_s:
    ll      r0,     f3_s - f2_e
    lc      r2,     -3
    ll      r3,     0xfe7e
    str     [r2],   r3
    # Aligned on 6 * 64
    fork
    bz      r0
f2_e:

    ll      r0,     for_s - f2_help
    b       r0
f2_help:


    %% nop 27


#
#  Fork 3
# --------
#

f3_s:
    ll      r0,     f4_s - f3_e
    lc      r2,     -3
    ll      r3,     0xfe7e
    str     [r2],   r3
    # Aligned on 7 * 64
    fork
    bz      r0
f3_e:

    ll      r0,     fback_s - f3_help
    b       r0
f3_help:


    %% nop 27


#
#  Fork 4
# --------
#
f4_s:
    ll      r0,     f5_s - f4_e
    lc      r2,     -3
    ll      r3,     0xfe7e
    str     [r2],   r3
    # Aligned on 8 * 64
    fork
    bz      r0
f4_e:

    ll      r0,     ffor_s - f4_help
    b       r0
f4_help:


    %% nop 49

#
#  Jump
# ------
#

    # Align on 9 * 64
    b       r4


#
#  Init forward
# --------------
#
#  registers:
#     r0:  dst jmp
#     r1:
#     r2:  dst write instr 1
#     r3:  dst write instr 2
#     r4:  instr 1 (commun): 'str [r2] r4; str'
#     r5:  instr 2: '[r3] r6; b ( r0 )'
#     r6:  instr 2: '[r3] r7; b ( r0 )'
#     r7:  instr 2: ...
#     r8:  instr 2: ...
#     r9:  instr 2: ...
#     r10: instr 2: ...
#     r11: instr 2: ...
#     r12: instr 2: ...
#     r13: instr 2: ...
#     r14: instr 2: ...
#     r15: instr 2: '[r3] r5; b ( r0 )'
#

for_s:
    mode    auricom
    ll      r0,     384 - 9
    ll      r2,     384 - 3
    ll      r3,     384 - 2


    ll      r4,     0xe42e
    ll      r5,     0x7f63
    ll      r6,     0x7f73
    ll      r7,     0x7f83
    ll      r8,     0x7f93
    ll      r9,     0x7fa3
    ll      r10,    0x7fb3
    ll      r11,    0x7fc3
    ll      r12,    0x7fd3
    ll      r13,    0x7fe3
    ll      r14,    0x7ff3
    ll      r15,    0x7f53

    # Timing
    stat    r1,     0
    # end

    ll      r1,     st_f_s - for_e
    b       r1
for_e:


#
#  Init fast forward
# -------------------
#
#  registers:
#     r0:  dst jmp
#     r1:
#     r2:
#     r3:  dst write instr
#     r4:
#     r5:  instr 1: 'b r4; b'
#     r6:  instr 1
#     r7:  instr 1
#     r8:  instr 1
#     r9:  instr 1
#     r10: instr 1
#     r11: instr 1
#     r12: instr 1
#     r13: instr 1
#     r14: instr 1
#     r15: instr 2: 'check, nop, b'
#

ffor_s:
    ll      r0,     384 - 6
    lc      r3,     -3

    ll      r5,     0xf47f
    mov     r6,     r5
    mov     r7,     r5
    mov     r8,     r5
    mov     r9,     r5
    mov     r10,    r5
    mov     r11,    r5
    mov     r13,    r5
    mov     r12,    r5
    mov     r14,    r5
    ll      r15,    0xf1cf

    # Timing
    ldb     [r0],   0,      61
    %% nop 6
    # end

    ll      r2,     st_f_s - ffor_e + 6
    mode    rocket
    b       r2
ffor_e:


    %% nop 30


#
#  Fork 5
# --------
#
f5_s:
    ll      r0,     beye_s - f5_e
    lc      r2,     -3
    ll      r3,     0xfe7e
    str     [r2],   r3
    # Aligned on 13 * 64
    fork
    bz      r0
f5_e:


#
#  Init Main Ship
# ----------------
#
#  registers:
#     r0:  dst jmp after check
#     r1:  dst small jmp
#     r2:
#     r3:
#     r4:  dst jmp
#     r5:
#     r6:
#     r7:
#     r8:
#     r9:
#     r10:
#     r11:
#     r12:
#     r13:
#     r14:
#     r15:
#

ship_s:
    mode    auricom
    ll      r0,     384 - 6
    ll      r1,     256 - 6
    ll      r4,     384 - 3

    # Timing
    ldb     [r0],   0,      0xd1
    write   r0

    %% nop 2
    # end

    ll      r5,    st_f_s - ship_e + 6
    mode    qirex
    b       r5
ship_e:


    %% nop 68


#
#  Starter forward
# -----------------
#

st_f_s:
    mode    rocket
    str     [r2],   r4
    # Align on 15 * 64
    str     [r3],   r11
    b       r0
st_f_e:


#
#  Init Bull's Eye
# -----------------
#
#  registers:
#     r0:  dst write 'f'
#     r1:  dst write instr 1
#     r2:  dst write instr 2
#     r3:  dst write instr 3
#     r4:  dst write instr 4
#     r5:  dst write instr 5
#     r6:  dst write instr 6
#     r7:  dst write instr 7
#     r8:  dst write instr 8
#     r9:
#     r10:
#     r11:
#     r12:
#     r13:
#     r14:
#     r15: unused address
#

beye_s:
    ldb     [r1],   0,      1
    write   r1

    ll      r0,     32064 - beye_e

    # Timing
    ll      r15,    1000
    rol     r14,    0
    # end

    mode    plasma

    # Timing
    mov     r14,    r14
    stb     [r15],  0,      10
    # end

    stb     [r0],   0,      1

beye_e:
