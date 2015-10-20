.text

# Generates an autostereogram inside of buffer
#
# Arguments:
#     autostereogram (unsigned char*)
#     depth_map (unsigned char*)
#     width
#     height
#     strip_size
calc_autostereogram:

        # Allocate 5 spaces for $s0-$s5
        # (add more if necessary)
        addiu $sp $sp -20
        sw $s0 0($sp)
        sw $s1 4($sp)
        sw $s2 8($sp)
        sw $s3 12($sp)
        sw $s4 16($sp)

        # autostereogram
        lw $s0 20($sp)
        # depth_map
        lw $s1 24($sp)
        # width
        lw $s2 28($sp)
        # height
        lw $s3 32($sp)
        # strip_size
        lw $s4 36($sp)

        # YOUR CODE HERE #
        li $t0 0 #i
        li $t1 0 #j
        li $t2 0 #offset for i, j
loop1:
        blt $t0 $s2 loop2
        j ending
loop2:
	blt $t1 $s3 cond
	addi $t0 $t0 1
	li $t1 0
	move $t2 $t0
	j loop1
cond:
	blt $t0 $s4 body1
	j body2
body1:
	addiu $sp $sp -16
	sw $t0 0($sp) 
	sw $t1 4($sp) 
	sw $t2 8($sp)
	sw $ra 12($sp)
	jal lfsr_random
	lw $t0 0($sp)
	lw $t1 4($sp)
	lw $t2 8($sp)
	lw $ra 12($sp)
	addiu $sp $sp 16
	addu $t3 $s0 $t2
	andi $v0 $v0 0xFF
	sb $v0 0($t3) # I(i, j) = random
	j loopend
body2:
	addu $t3 $s1 $t2
	lbu $t4 0($t3) # depthmap(i, j)
	add $t4 $t4 $t0 # $t4 + i
	sub $t4 $t4 $s4 # $t4 - S
	mul $t5 $t1 $s2 #t5 = j * width
	add $t5 $t5 $t4 #t5 = $t5 + newi
	add $t6 $s0 $t5 # pointer to correct ^
	lbu $t7 0($t6)
	addu $t3 $s0 $t2
	sb $t7 0($t3)
	j loopend
	
loopend:
	add $t2 $t2 $s2 #$t2 = $t2 + width
	addiu $t1 $t1 1
	j loop2
ending:
        lw $s0 0($sp)
        lw $s1 4($sp)
        lw $s2 8($sp)
        lw $s3 12($sp)
        lw $s4 16($sp)
        addiu $sp $sp 20
        jr $ra
        
        