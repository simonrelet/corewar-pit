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


    mode    assegai                         # 0 + 3

    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop     # 3 + 39


#
#  Fork 1
# --------
#

f1_s:
    ll      r0,     f2_s - f1_e             # 42 + 7
    lc      r2,     -3                      # 49 + 5
    ll      r3,     0xfe7e                  # 54 + 7
    str     [r2],   r3                      # 61 + 3
    # Aligned on 1 * 64
    fork                                    # 64 + 2
    bz      r0                              # 66 + 3

f1_e:
    ll      r0,     back_s - f1_help        # 69 + 7
    b       r0                              # 76 + 3
f1_help:


    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop                     # 79 + 35


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
    ll      r0,     -384 - 6                # 114 + 7
    or      r1,     r0                      # 121 + 3
    lc      r3,     -3                      # 124 + 5

    ll      r5,     0xf1cf                  # 129 + 7
    ll      r6,     0xf47f                  # 136 + 7
    mov     r7,     r6                      # 143 + 3
    mov     r8,     r6                      # 146 + 3
    mov     r9,     r6                      # 149 + 3
    mov     r10,    r6                      # 152 + 3
    mov     r11,    r6                      # 155 + 3
    mov     r12,    r6                      # 158 + 3
    mov     r13,    r6                      # 161 + 3
    mov     r14,    r6                      # 164 + 3
    mov     r15,    r6                      # 167 + 3

    # Timing
    ldb     [r0],   0,      68              # 170 + 7
    mov     r0,     r0                      # 177 + 3
    mov     r0,     r0                      # 180 + 3

    nop nop nop nop nop                     # 183 + 5
    # end

    ll      r2,     st_b_s - fback_e + 6    # 188 + 7
    mode    rocket                          # 195 + 3
    b       r2                              # 198 + 3
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
    mode    auricom                         # 201 + 3
    ll      r0,     -384 - 9                # 204 + 7
    or      r1,     r0                      # 211 + 3
    ll      r2,     -384 - 3                # 214 + 7
    ll      r3,     -384 - 2                # 221 + 7

    ll      r4,     0xe42e                  # 228 + 7
    ll      r5,     0x7ff3                  # 235 + 7
    ll      r6,     0x7f53                  # 242 + 7
    ll      r7,     0x7f63                  # 249 + 7
    ll      r8,     0x7f73                  # 256 + 7
    ll      r9,     0x7f83                  # 263 + 7
    ll      r10,    0x7f93                  # 270 + 7
    ll      r11,    0x7fa3                  # 277 + 7
    ll      r12,    0x7fb3                  # 284 + 7
    ll      r13,    0x7fc3                  # 291 + 7
    ll      r14,    0x7fd3                  # 298 + 7
    ll      r15,    0x7fe3                  # 305 + 7

    # Timing
    nop nop                                 # 312 + 2
    # end

back_e:


#
#  Starter backward
# ------------------
#

st_b_s:
    mode    rocket                          # 314 + 3
    str     [r2],   r4                      # 317 + 3
    # Align on 5 * 64
    str     [r3],   r5                      # 320 + 3
    b       r1                              # 323 + 3
st_b_e:


    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop                 # 326 + 36


#
#  Fork 2
# --------
#

f2_s:
    ll      r0,     f3_s - f2_e             # 362 + 7
    lc      r2,     -3                      # 369 + 5
    ll      r3,     0xfe7e                  # 374 + 7
    str     [r2],   r3                      # 381 + 3
    # Aligned on 6 * 64
    fork                                    # 384 + 2
    bz      r0                              # 386 + 3
f2_e:

    ll      r0,     for_s - f2_help         # 389 + 7
    b       r0                              # 396 + 3
f2_help:


    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop             # 399 + 27


#
#  Fork 3
# --------
#

f3_s:
    ll      r0,     f4_s - f3_e             # 426 + 7
    lc      r2,     -3                      # 433 + 5
    ll      r3,     0xfe7e                  # 438 + 7
    str     [r2],   r3                      # 445 + 3
    # Aligned on 7 * 64
    fork                                    # 448 + 2
    bz      r0                              # 450 + 3
f3_e:

    ll      r0,     fback_s - f3_help       # 453 + 7
    b       r0                              # 460 + 3
f3_help:


    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop             # 463 + 27


#
#  Fork 4
# --------
#
f4_s:
    ll      r0,     f5_s - f4_e             # 490 + 7
    lc      r2,     -3                      # 497 + 5
    ll      r3,     0xfe7e                  # 502 + 7
    str     [r2],   r3                      # 509 + 3
    # Aligned on 8 * 64
    fork                                    # 512 + 2
    bz      r0                              # 514 + 3
f4_e:

    ll      r0,     ffor_s - f4_help        # 517 + 7
    b       r0                              # 524 + 3
f4_help:


    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop     # 527 + 49

#
#  Jump
# ------
#

    # Align on 9 * 64
    b       r4                              # 576 + 3


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
    mode    auricom                         # 579 + 3
    ll      r0,     384 - 9                 # 582 + 7
    ll      r2,     384 - 3                 # 589 + 7
    ll      r3,     384 - 2                 # 596 + 7


    ll      r4,     0xe42e                  # 603 + 7
    ll      r5,     0x7f63                  # 610 + 7
    ll      r6,     0x7f73                  # 617 + 7
    ll      r7,     0x7f83                  # 624 + 7
    ll      r8,     0x7f93                  # 631 + 7
    ll      r9,     0x7fa3                  # 638 + 7
    ll      r10,    0x7fb3                  # 645 + 7
    ll      r11,    0x7fc3                  # 652 + 7
    ll      r12,    0x7fd3                  # 659 + 7
    ll      r13,    0x7fe3                  # 666 + 7
    ll      r14,    0x7ff3                  # 673 + 7
    ll      r15,    0x7f53                  # 680 + 7

    # Timing
    stat    r1,     0                       # 687 + 4
    # end

    ll      r1,     st_f_s - for_e          # 691 + 7
    b       r1                              # 698 + 3
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
    ll      r0,     384 - 6                 # 701 + 7
    lc      r3,     -3                      # 708 + 5

    ll      r5,     0xf47f                  # 713 + 7
    mov     r6,     r5                      # 720 + 3
    mov     r7,     r5                      # 723 + 3
    mov     r8,     r5                      # 726 + 3
    mov     r9,     r5                      # 729 + 3
    mov     r10,    r5                      # 732 + 3
    mov     r11,    r5                      # 735 + 3
    mov     r13,    r5                      # 738 + 3
    mov     r12,    r5                      # 741 + 3
    mov     r14,    r5                      # 744 + 3
    ll      r15,    0xf1cf                  # 747 + 7

    # Timing
    ldb     [r0],   0,      61              # 754 + 7
    nop nop nop nop nop nop                 # 761 + 6
    # end

    ll      r2,     st_f_s - ffor_e + 6     # 767 + 7
    mode    rocket                          # 774 + 3
    b       r2                              # 777 + 3
ffor_e:


    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop # 780 + 30


#
#  Fork 5
# --------
#
f5_s:
    ll      r0,     beye_s - f5_e           # 810 + 7
    lc      r2,     -3                      # 817 + 5
    ll      r3,     0xfe7e                  # 822 + 7
    str     [r2],   r3                      # 829 + 3
    # Aligned on 13 * 64
    fork                                    # 832 + 2
    bz      r0                              # 834 + 3
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
    mode    auricom                         # 837 + 3
    ll      r0,     384 - 6                 # 840 + 7
    ll      r1,     256 - 6                 # 847 + 7
    ll      r4,     384 - 3                 # 854 + 7

    # Timing
    ldb     [r0],   0,      0xd1            # 861 + 7
    write   r0                              # 868 + 3

    nop nop                                 # 871 + 2
    # end

    ll      r5,    st_f_s - ship_e + 6      # 873 + 7
    mode    qirex                           # 880 + 3
    b       r5                              # 883 + 3
ship_e:


    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop         # 886 + 68


#
#  Starter forward
# -----------------
#

st_f_s:
    mode    rocket                          # 954 + 3
    str     [r2],   r4                      # 957 + 3
    # Align on 15 * 64
    str     [r3],   r11                     # 960 + 3
    b       r0                              # 963 + 3
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
    ldb     [r1],   0,      1               # 966 + 7
    write   r1                              # 973 + 3

    ll      r0,     32064 - beye_e          # 976 + 7

    # Timing
    ll      r15,    1000                    # 983 + 7
    rol     r14,    0                       # 990 + 3
    # end

    mode    plasma                          # 993 + 3

    # Timing
    mov     r14,    r14                     # 996 + 3
    stb     [r15],  0,      10              # 999 + 7
    # end

    stb     [r0],   0,      1               # 1006 + 7
beye_e:
