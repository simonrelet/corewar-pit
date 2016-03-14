# corewar-pit

The Pit is the assembler program of the Corewar Championship, it will transform
your assembly ship into a core ship. This is the first step to becoming a
legend.

One can feel strong enough to write a ship directly in the core format, but
for the others you should write it in the assembly language described here and
use the Pit to create the core ship.

## The Assembly Syntax
### The Basics

To be valid, an assembly ship must:
  * Have a name and contains at least one instruction;
  * Only have one instruction, label or guildeline per line.

#### Line Comments

Line comments are characters ignored by the Pit and will therfore not be
visible in the core ship. They start at the first `#` character and will end
at the first line ending or at the end of the file.

For example:
```
mov r4, r6  # It's gonna rock!
```

#### Instruction

An instruction is composed of an opcode followed by arguments. The arguments
are separated by commas (`,`).

For example:
```
mov r4, r15    # Opcode 'move' with arguments 'r4' and 'r15'
mov r4 r15     # Syntax error
mov, r4, r15   # Syntax error
mov r4, , r15  # Syntax error
```

#### The Nibble: Smalest Memory Unit

Through the Corewar Championship the smalest memory unit used is the nibble
('q', also know as a quartet). It has a length of 4 bits (half-byte) and can be
represented by a single hexadecimal character.

So `0xf` is a nibble, and '4q' means '4 nibbles'.

#### The PC and W'O"

The PC is the program counter of the ship, it's value can increase or decrease
depending on the jumps and will always be in the range [0, 65 535].

The W'O" is the distance counter, it can only increase. This value represent
the distance traveled since the start of the race.

#### The Registers

Any ship has access to 16 registers of 4q. The first register is `r0` and the
last `r15`.

By default they are all set to zero.

#### The Buffer

A buffer is availble to any ship to store up to 64q. There are only 2
instructions to access it: `ldb` and `stb`. The buffer is circular, so writing
or reading past the end, will loop back to the start of the buffer. 

By default the buffer is set to zero.

#### The Flags

There are 2 flags (`Z` and `S`) of 1 bit which cannot be set directly. Some
instructions can updates theses flags depending on the result (`add` for
example) and others can test them (`bnz` for example).

The `Z` flag is set to `1` or `0` to notify whether the result is equals to `0`
or not, respectively.

The `S` flag is set to `1` or `0` to notify whether the result is strictly
negative or not, respectively.

By default `Z` and `S` are not set (`0`).

#### The Ship Modes

The ship mode is used by the Stadium to perform some optimisations. Depending
on it, some instructions will decode and/or execute faster while others can be
slower. It can be changed using the `mode` instruction.

Choose wisely the mode(s) of your ship.

#### The Modes Visibility: IDX

A ship's visibility isn't absolute, it cannot see everything on the track.
The current mode of the ship defines the visibility it has: the IDX.

The follwing rules are applies for read, write and jumps:
  * The lowest accessible address is PC - IDX;
  * The highest accessible address is PC + IDX - 1;
  * The accessible range is circular and 2 * IDX wide.

For example a ship in a mode with an IDX: 10 and a PC: 30 can read, write or
jump in the range [20, 39], which makes 20 possible addresses. This range is a
circular subset of the track which means that if the ships try to jumps with an
offset of 10 (to address 40) it will end up at address 20.

#### The Constants

Some instructions take constants as arguments, it's size (the number of
nibbles) and whether it is signed or unsigned (positive, negative) is defined
by the instruction.

A constant can be in decimal or in hexadecimal but then must be preceed by
`0x`.

For example:
```
ll r1, 1234
ll r1, 0xCaFe  # No case sensitivity.
ll r1, 1fff    # Syntax error
ll r1, 000042  # Just 42
ll r1, -1      # Still working
```

#### The labels

A label is an identifier whose value is the address of it's definition and can
be used as constants in any expressions. In the assembly file a label can be
referenced at any line, it doesn't have to be after it's definition.

The Pit will replace every use of a label by it's value before any expression
evaluation. There are only here to help writting and reading an assembly ship,
they were not be visible in the core ship.

A label must:
  * Start with a letter, can contains letters, numbers, or `_` and must finish
    with a letter or a number;
  * Be defined only once.

By the way, the first instruction of the assembly file is at address `0x0`.

Value examples:
```
foo:            # 'foo' equals to 0x0
    mov r0, r1
bar:            # 'bar' equals to 0x3,
                # because 'mov' has a size of 3q.
    nop
    nop
    nop
yeah:           # 'yeah' equals to 0x6,
                # because 'nop' has a size of 1.
```

Label definition examples:
```
kl34:   # This label is valid
K_4_Z:  # This one too
_mnp:   # Syntax error
mnp_:   # Syntax error
3go:    # Syntax error
kl34:   # Syntax error: already defined
```

#### The Expressions

Each time an instruction requires a constant value, an expression can be used.
They can contains constants values, labels and perform arithmetic operations
with them.

For example:
```
foo:
    ll r0, foo - (1 + 2 * 4) / 8    # Works with simple operators
    ll r0, foo ^ 2                  # Exponents also works
    ll r0, bar ^ (foo - 0xa) / bar  # Messy, but ok
bar:
```

#### The Guidelines

A guideline is an additional information concerning the ship, they can be seen
as 'meta-data'. They can be used anywhere in the code even though it is usual
to put them at the top of the assembly file. Using the same guildeline several
times won't create errors, only the last one found will be used. A guildeline
is case insensitive.

The Pit recognise the folowing guildlines:

##### Name
Syntax: `.name "Corewar 101"`. Max length: 32 characters.

Sets the name of the ship. This guildeline is mandatory for a ship. If the name
is bigger than 32 characters it will be truncated and a warning will be
produced.

Examples:
```
.name "Don't worry guys"         # Single quotes are valid
.name "The name is \"Awesome\""  # Double quotes must be escaped
.name "The \\"                   # Escape the backslash to display it
```

##### Comment
Syntax: `.comment "This ship is awesome"`. Max length: 128 characters.

Sets a comment for the ship. This guildeline is optional for a ship. If the
comment is bigger than 128 characters it will be truncated and a warning will
be produced.

Examples:
```
.comment "Don't worry guys"         # Single quotes are valid
.comment "The name is \"Awesome\""  # Double quotes must be escaped
.comment "The \\"                   # Escape the backslash to display it
```

### The Instruction Set

In the bellow descriptions, `rx` and `ry` refers to any register.
The 'Encoding' represent the instruction in the core format.

#### Logical Instructions

##### AND
Syntax: `and rx, ry`. Flags update: `Z`, `S`. Encoding: `2xy`

Computes the bitwise logical AND between `rx` and `ry`, and stores the result
in `rx`.

Example:
```
ll  r1, 0x83cb
ll  r2, 0x7ac3
and r1, r2      # r1: 0x02c3
```

##### OR
Syntax: `or rx, ry`. Flags update: `Z`, `S`, Encoding: `3xy`

Computes the bitwise logical OR between `rx` and `ry`, and stores the result in
`rx`.

Example:
```
ll r1, 0x83cb
ll r2, 0x7ac3
or r1, r2      # r1: 0xfbcb
```

##### XOR
Syntax: `xor rx, ry`. Flags update: `Z`, `S`. Encoding: `4xy`

Computes the bitwise exclusive OR between `rx` and `ry`, and stores the result
in `rx`.

Example:
```
ll  r1, 0x83cb
ll  r2, 0x7ac3
xor r1, r2      # r1: 0xf908
```

##### NOT
Syntax: `not rx, ry`. Flags update: `Z`, `S`. Encoding: `5xy`

Computes the binary negation of `ry`, and stores the result in `rx`.

Example:
```
ll  r2, 0x7ac3
not r1, r2      # r1: 0x853c
```

##### ROL (ROtation Left)
Syntax: `rol rx, n`. Flags update: `Z`, `S`. Encoding: `6xn`

Does a left rotation of `n` bits on the `rx` register. Bits that exit on the
left enter on the right.

`n` is unsigned and encoded on 1q, which means that 0x1234 will be truncated to
0x4.

Examples:
```
ll  r1, 0x83cb
rol r1, 3       # r1: 0x1e5c
ll  r1, 0x83cb
rol r1, 0xf143  # Same result
```

#### Arithmetic Instructions
##### ASR (Arithmetic Shift Right)
Syntax: `asr rx, n`. Flags Update: `Z`, `S`. Encoding: `7xn`

Does an arithmetic shift of `n` bits to the right on the `rx` register:
entering bits are equal to the sign bit.

`n` is unsigned and encoded on 1q, which means that 0x1234 will be truncated to
0x4.

Examples:
```
ll  r1, 0x3333
asr r1, 2       # r1: 0x0ccc
ll  r1, 0xcccc
asr r1, 2       # r1: 0xf333
ll  r1, 0xcccc
asr r1, 0xa2    # Same result
```

##### ADD
Syntax: `add rx, ry`. Flags Update: `Z`, `S`. Encoding: `8xy`

Adds `ry`'s content to `rx`'s, and stores the result in `rx`.

Example:
```
ll  r1, 1
ll  r2, -2
add r1, r2  # r1: -1 (0xffff)
```

##### SUB
Syntax: `sub rx, ry`. Flags Update: `Z`, `S`. Encoding: `9xy`

Subtracts `ry`'s content from `rx`'s, and stores the result in `rx`.

Example:
```
ll  r1, 1
ll  r2, 2
sub r1, r2  # r1: -1 (0xffff)
```

##### CMP (CoMPare)
Syntax: `cmp rx, ry`. Flags Update: `Z`, `S`. Encoding: `axy`

Subtracts `ry`'s content from `rx`'s, but doesn't store the result.

Example:
```
ll  r1, 1
ll  r2, 2
cmp r1, r2  # r1: 1 (0x0001)
```

##### ADDI (Add Integer)
Syntax: `addi rx, n`. Flags Update: `Z`, `S`. Encoding: `f5xn`

Adds the signed constant `n` to the `rx` register, and stores the result in
`rx`.

`n` is encoded on 1q, which means that 0x1234 will be truncated to 0x4.

Examples:
```
lc   r1, 1
addi r1, 0xf     # r1: 0
lc   r1, 2
addi r1, 0xff00  # r1: 2
```

##### CMPI (CoMPare to Integer)
Syntax: `cmpi rx, n`. Flags Update: `Z`, `S`. Encoding: `f6xn`

Subtracts the signed constant `n` from `rx`, but doesn't store the result.

`n` is encoded on 1q, which means that 0x1234 will be truncated to 0x4.

Example:
```
lc   r1, 1
cmpi r1, 1  # r1: 1
```

##### NEG (NEGate)
Syntax: `neg rx, ry`. Flags Update: `Z`, `S`. Encoding: `bxy`

Stores the opposite of `ry` in `rx`.

Example:
```
lc  r2, 1
neg r1, r2  # r1: -1 (0xffff)
```

#### Data move Instructions
##### MOV (MOVe)
Syntax: `mov rx, ry`. Flags Update: None. Encoding: `cxy`

Copies the content of `ry` to `rx`.

Example:
```
ll  r1, 0xcafe
mov r2, r1      # r2: 0xcafe
```

##### SWP (SWaP)
Syntax: `swp rx, ry`. Flags Update: None. Encoding: `f4xy`

Swaps the content of the `rx` and `ry` registers.

Example:
```
ll r1, 2097
ll r2, 6100
swp r1, r2   # r1: 6100, r2: 2097
```

##### LC (Load Constant)
Syntax: `lc rx, n`. Flags Update: None. Encoding: `f2xnn`

Loads `n` in `rx`'s two least significant nibbles and propagates the sign bit
to `rx`'s two most significant nibbles.

`n` is encoded on 2q, which means that 0x1234 will be truncated to 0x34.

Examples:
```
lc r1, 10      # r1: 10 (0x000a)
lc r2, -10     # r2: -10 (0xfff6)
lc r3, 0x00f6  # r3: -10 (0xfff6)
```

##### LL (Load Long Constant)
Syntax: `ll rx, n`. Flags Update: None. Encoding: `f3xnnnn`

Loads `n` in the `rx` register. `n` is encoded on 4q.

Examples:
```
ll r1, 10      # r1: 10 (0x000a)
ll r2, -10     # r2: -10 (0xfff6)
ll r3, 0x00f6  # r3: 0x00f6
```

#### Memory Instructions

In order to simplify the notation, the expression `rx % IDX` will designate the
special modulo used by the Stadium to access the memory. This is *NOT* the
usual arithmetic modulo.

##### LDR (LoaD Register)
Syntax: `ldr rx, [ry]`. Flags Update: None. Encoding: `dxy`

Loads the 4q stored at the address `PC + (ry % IDX)` in `rx`.

`ry`'s value is signed.

Example:
```
ll  r0, 20
ldr r1, [r0]  # Reads r1's content from PC + (20 % IDX)
```

##### STR (STore Register)
Syntax: `str [rx], ry`. Flags Update: None. Encoding: `exy`

Stores the 4q of `ry` at the address `PC + (rx % IDX)`.

`rx`'s value is signed.

Example:
```
ll  r0, 20
ll  r1, 0x1234
str [r0], r1    # Writes r1's content at PC + (20 % IDX)
```

##### LDB (LoaD Buffer)
Syntax: `ldb [rx], n, m`. Flags Update: None. Encoding: `f0xnnmm`

Reads, starting at address `PC + (rx % IDX)`, the first `m` quartets and stores
them starting at the offset `n` in the buffer. As the buffer is circular, if
`m` is greater than the distance between the offset `n` and the end of the
buffer, overflowing nibbles will be written at the beginning of the buffer.

`m` and `n` are unsigned, `rx`'s content is signed.

Example:
```
ll  r0, 42
ldb [r0], 0, 32  # Writes at the begining of the buffer the 32
                 # first nibbles starting at PC + (42 % IDX)
```

##### STB (STore Buffer)
Syntax: `stb [rx], n, m`. Flags Update: None. Encoding: `f1xnnmm`

Writes, starting at address `PC + (rx % IDX)`, `m` quartets of the buffer
starting at offset `n`. As the buffer is circular, if `m` is greater than the
distance between the offset `n` and the end of the buffer, the overflowing
nibbles will be read from the beginning of the buffer.

`m` and `n` are unsigned, `rx`'s content is signed.

Example:
```
ll  r0, 42
stb [r0], 0, 32  # Writes the first 32q of the buffer at
                 # PC + (42 % IDX)
```

#### Branch Instructions

##### B (Branch)
Syntax: `b rx`. Flags Update: None. Encoding: `f7x`

Does an systematic jump of `rx` nibbles (`PC = PC + (rx % IDX)`).

`rx`'s content is signed.

Example:
```
ll r0, 40
b  r0      # Jumps to PC + (40 % IDX)
```

##### BZ (Branch if Zero flag)
Syntax: `bz rx`. Flags Update: None. Encoding: `f8x`

Does a jump of `rx` nibbles if the flag `Z` is set (`PC = PC + (rx % IDX)`).

`rx`'s conent is signed.

Example:
```
ll  r0, 40
add r1, r1  # Sets the Z flag, by default registers are at 0x0000
bz  r0      # Jumps to PC + (40 % IDX)
```

##### BNZ (Branch if Not Zero flag)
Syntax: `bnz rx`. Flags Update: None. Encoding: `f9x`

Does a jump of `rx` nibbles if the flag `Z` is not set
(`PC = PC + (rx % IDX)`).

`rx`'s content is signed.

Example:
```
ll   r0, 40
addi r1, 1  # Unsets the Z flag
bnz  r0     # Jumps to PC + (40 % IDX)
```

##### BS (Branch if Sign flag)
Syntax: `bs rx`. Flags Update: None. Encoding: `fax`

Does a jump of `rx` nibbles if the flag `S` is set (`PC = PC + (rx % IDX)`).

`rx`'s content is signed.

Example:
```
ll   r0, 40
addi r1, -1  # Sets the S flag
bs   r0      # Jumps to PC + (40 % IDX)
```

#### System Instructions

##### CRASH
Syntax: `crash`. Flags Update: None. Encoding: `0`

If a processor executes this instruction, it is instantly destroyed (but we
don't really care, do we?). Consecutive `crash`s can be written on the same line.

Examples:
```
crash              # Bang! You're dead.
crash crash crash  # Still dead.
```

##### NOP
Syntax: `nop`. Flags Update: None. Encoding: `1`

No effect, very useful. Consecutive `nop`s can be written on the same line.

Example:
```
nop nop nop nop nop  # Well, you got the idea.
```

##### WRITE
Syntax: `write rx`. Flags Update: None. Encoding: `ffx`

Displays `rx`'s content on the standard output stream.

##### STAT
Syntax: `stat rx, n`. Flags Update: None. Encoding: `fbxn`

Loads the Stadium's statistics in `rx`.

| Instruction   |  Effect   |
|---------------|-----------|
| `stat rx, 0`  | sets to 0 |
| `stat rx, 1`  | loads the Stadium's mode |
| `stat rx, 2`  | loads `PC` |
| `stat rx, 3`  | loads `W'O"` |
| `stat rx, 4`  | loads the number of validated checkpoints since the beginning of the race |
| `stat rx, 5`  | loads the start address of the ship |
| `stat rx, 6`  | loads the track size (in q) |
| `stat rx, 7`  | loads the number of laps to complete |
| `stat rx, 8`  | loads the number of checkpoints per lap |
| `stat rx, 9`  | loads the size of a checkzone (in q) |
| `stat rx, 10` | loads the maximum number of cycles to validate a checkpoint |
| `stat rx, 11` | loads the remaining number of cycles to validate a checkpoint before the ship is destroyed |
| `stat rx, 12` | loads the ship's position in the race (1 if it's first, 2 if it's second etc.) |
| `stat rx, 13` | loads the distance between the ship and the closest locked ship |
| `stat rx, 14` | loads the locked ship's mode |
| `stat rx, 15` | copies the content of `Z` in the bit 0 (least significant bit), and copies the content of `S` in the bit 1 |

##### CHECK
Syntax: `check`. Flags Update: `Z`. Encoding: `fc`

Tries to validate a checkzone. If the check succeeds, `Z` is set.

##### MODE
Syntax: `mode m`. Flags Update: None. Encoding: `fdm`

Changes the current mode. `m` can either be the name of the mode (case
insensitive) or the mode numeric value. Valid modes are: 
  * 0: `feisar`
  * 1: `goteki45`
  * 2: `agsystems`
  * 3: `auricom`
  * 4: `assegai`
  * 5: `piranha`
  * 6: `qirex`
  * 7: `icaras`
  * 8: `rocket`
  * 9: `missile`
  * 10: `mines`
  * 11: `plasma`
  * 12: `miniplasma`

If `m` is bigger than 12, the mode doesn't change.

##### FORK
Syntax: `fork`. Flags Update: `Z`. Encoding: `fe`

Duplicates the ship. The only differences between the 2 ships is the `Z` flag:
The new ship's `Z` flag is set and the original ship's `Z` flag is not set.

## Encoding

| Instruction      | q0 | q1 | q2 | q3 | q4 | q5 | q6 |
|------------------|----|----|----|----|----|----|----|
| `crash`          |  0 |    |    |    |    |    |    |
| `nop`            |  1 |    |    |    |    |    |    |
| `and rx, ry`     |  2 | rx | ry |    |    |    |    |
| `or rx, ry`      |  3 | rx | ry |    |    |    |    |
| `xor rx, ry`     |  4 | rx | ry |    |    |    |    |
| `not rx, ry`     |  5 | rx | ry |    |    |    |    |
| `rol rx, n`      |  6 | rx |  n |    |    |    |    |
| `asr rx, n`      |  7 | rx |  n |    |    |    |    |
| `add rx, ry`     |  8 | rx | ry |    |    |    |    |
| `sub rx, ry`     |  9 | rx | ry |    |    |    |    |
| `cmp rx, ry`     |  a | rx | ry |    |    |    |    |
| `neg rx, ry`     |  b | rx | ry |    |    |    |    |
| `mov rx, ry`     |  c | rx | ry |    |    |    |    |
| `ldr rx, [ry]`   |  d | rx | ry |    |    |    |    |
| `str [rx], ry`   |  e | rx | ry |    |    |    |    |
| `ldb [rx], n, m` |  f |  0 | rx | n0 | n1 | m0 | m1 |
| `stb [rx], n, m` |  f |  1 | rx | n0 | n1 | m0 | m1 |
| `lc rx, n`       |  f |  2 | rx | n0 | n1 |    |    |
| `ll rx, n`       |  f |  3 | rx | n0 | n1 | n2 | n3 |
| `swp rx, ry`     |  f |  4 | rx | ry |    |    |    |
| `addi rx, n`     |  f |  5 | rx |  n |    |    |    |
| `cmpi rx, n`     |  f |  6 | rx |  n |    |    |    |
| `b rx`           |  f |  7 | rx |    |    |    |    |
| `bz rx`          |  f |  8 | rx |    |    |    |    |
| `bnz rx`         |  f |  9 | rx |    |    |    |    |
| `bs rx`          |  f |  a | rx |    |    |    |    |
| `stat rx, n`     |  f |  b | rx |  n |    |    |    |
| `check`          |  f |  c |    |    |    |    |    |
| `mode m`         |  f |  d |  m |    |    |    |    |
| `fork`           |  f |  e |    |    |    |    |    |
| `write rx`       |  f |  f | rx |    |    |    |    |
