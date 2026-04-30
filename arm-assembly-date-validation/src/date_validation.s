		;		Yeb Woei Lun (243UC245MN)
		;       Bianca Lau Ying Xuan (242UC2426R)
		
		;		move r0,#0x11111111
		mov		r0,#0x11000000
		mov		r1,#0x00110000
		mov		r2,#0x00001100
		mov		r3,#0x00000011
		add		r0, r0, r1
		add		r0, r0, r2
		add		r0, r0, r3
		mov		r8,#0x2000
		str		r0, [r8]
		
		;		move r0,#0x22223333
		mov		r0,#0x22000000
		mov		r1,#0x00220000
		mov		r2,#0x00003300
		mov		r3,#0x00000033
		add		r0, r0, r1
		add		r0, r0, r2
		add		r0, r0, r3
		str		r0, [r8,#4]
		
		;		move r0,#0x31111111
		mov		r0,#0x31000000
		mov		r1,#0x00110000
		mov		r2,#0x00001100
		mov		r3,#0x00000011
		add		r0, r0, r1
		add		r0, r0, r2
		add		r0, r0, r3
		str		r0, [r8,#8]
		
		;		move r0,#0x42223333
		mov		r0,#0x42000000
		mov		r1,#0x00220000
		mov		r2,#0x00003300
		mov		r3,#0x00000033
		add		r0, r0, r1
		add		r0, r0, r2
		add		r0, r0, r3
		str		r0, [r8,#12]
		
		;		move r0,#0x51111111
		mov		r0,#0x51000000
		mov		r1,#0x00110000
		mov		r2,#0x00001100
		mov		r3,#0x00000011
		add		r0, r0, r1
		add		r0, r0, r2
		add		r0, r0, r3
		str		r0, [r8,#16]
		
		;		move r0,#0x62223333
		mov		r0,#0x62000000
		mov		r1,#0x00220000
		mov		r2,#0x00003300
		mov		r3,#0x00000033
		add		r0, r0, r1
		add		r0, r0, r2
		add		r0, r0, r3
		str		r0, [r8,#20]
		
		;		move r0,#0x71111111
		mov		r0,#0x71000000
		mov		r1,#0x00110000
		mov		r2,#0x00001100
		mov		r3,#0x00000011
		add		r0, r0, r1
		add		r0, r0, r2
		add		r0, r0, r3
		str		r0, [r8,#24]
		
		;		move r0,#0x82223333
		mov		r0,#0x82000000
		mov		r1,#0x00220000
		mov		r2,#0x00003300
		mov		r3,#0x00000033
		add		r0, r0, r1
		add		r0, r0, r2
		add		r0, r0, r3
		str		r0, [r8,#28]
		
		;		move r0,#0x91111111
		mov		r0,#0x91000000
		mov		r1,#0x00110000
		mov		r2,#0x00001100
		mov		r3,#0x00000011
		add		r0, r0, r1
		add		r0, r0, r2
		add		r0, r0, r3
		str		r0, [r8,#32]
		
		;		move r0,#0xA2223333
		mov		r0,#0xA2000000
		mov		r1,#0x00220000
		mov		r2,#0x00003300
		mov		r3,#0x00000033
		add		r0, r0, r1
		add		r0, r0, r2
		add		r0, r0, r3
		str		r0, [r8,#36]
		
		
		
		;Function	check BCD valid or not =================================================
		
		MOV		R8, #0x2000 ;Reset memory place for later to store
		MOV		R10, #10 ;10 iteration for 10 value
		
loop
		LDR		R0, [R8] ;take the value to check
		
		MOV		R1, #0 ;reset r1
		MOV		R2, #8 ;8 numbers to compare, Exp: #0x11111111
		MOV		R3, #28 ;32bit - 4 bit(byte) = 28 (the fuction will check number 1 by 1, each time use 4 bit)
		
check
		MOV		R4, R0, LSR R3 ;masking left 4 bit
		AND		R4, R4, #0xF ;remain the 4 bit
		
		CMP		R4, #0xA ;compare the value whether within 0-9
		BLT		valid ;if less then (within)
		
		MOV		R4, #1 ;if excess become 1
		
valid
		ADD		R1, R1, R4, LSL R3 ;put back the value
		
		SUB		R3, R3, #4 ;update for next number
		SUB		R2, R2, #1 ;Indicate 1 number done
		CMP		R2,#0 ;check if all number already compared
		BNE		check
		
		STR		R1, [R8], #4 ;store value and add 4 to compare next value
		
		SUB		R10, R10, #1
		CMP		R10,#0
		BNE		loop
		
		;End		function ===================================================================
		
		
		
		
		
		
		END
