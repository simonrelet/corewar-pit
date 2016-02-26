.name "Freaky Owl"
.comment "I Want It Owl"

# Comment
begin:
	mode	ASSEGAI

	ll	r1, 45
	lc	r2, 7
	ldr	r3, [r1]
	ldb	[r2], 0, 10

	mode	FEISAR

	add	r0, r2
	addi	r1, 5
	sub	r0, r5
	swp	r0, r5

	and	r3, r2
	asr	r2, 4
	rol	r3, 2

	b	r4
	bz	r3
	bnz	r2
	bs	r0

	check
	crash
	fork
	nop

	cmp	r3, r4
	cmpi	r2, 2

	mov	r3, r4
	neg	r0, r1
	not	r1, r0
	or	r1, r2
	xor	r5, r6
	stat	r3, 14

	stb	[r3], 0, 10
	str	[r2], r0

	write	r5