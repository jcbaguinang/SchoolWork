.data

lfsr:
        .align 4
        .half
        0x1

.text

# Implements a 16-bit lfsr
#
# Arguments: None
lfsr_random:

        la $t0 lfsr
        lhu $v0 0($t0)
        # YOUR CODE HERE #
        addi $t1 $0 16 #i = 16
        j loop
loop:
	bgt $t1 $0 cond1
	j end
	
cond1:
	srl $t2 $v0 0
	srl $t3 $v0 2
	srl $t4 $v0 3
	srl $t5 $v0 5
	xor $t4 $t4 $t5
	xor $t3 $t3 $t4
	xor $t2 $t2 $t3
	sll $t2 $t2 15
	srl $t3 $v0 1
	or $t2 $t3 $t2
	and $t2 $t2 0xFFFF
	move $v0 $t2
	subi $t1 $t1 1
	j loop
end:
        la $t0 lfsr
        sh $v0 0($t0)
        jr $ra
