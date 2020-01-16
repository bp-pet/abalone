#implement players
#implement pushing

class Board:

	def __init__(self, fields = [], selection = []):
		self.DIM = 5
		if fields == []:
			self.fields = self.makeFields()

	def makeFields(self):
		fields = dict();
		for x in range(-self.DIM, self.DIM + 1):
			for y in range(-self.DIM, self.DIM + 1):
				if (x * y < 0 and abs(x) + abs(y) > self.DIM - 1) \
					or x >= self.DIM or y >= self.DIM \
					or x <= -self.DIM or y <= -self.DIM:
					fields[(x, y)] = Field(0, (x, y))
				else:
					fields[(x,y)] = Field(1, (x, y))
		return fields

	def moveCursor(self, x, y):
		self.fields()[cursorPos].setCursor = False
		newPos = (self.cursorPos[0] + x, self.cursorPos[1] + y)
		if self.isValidField():
			self.fields()[cursorPos].setCursor = True

	def deepCopy(self):
		return Board(self.fields, self.selection)

	def isValidField1(self, coords):
		return self.isValidField2(coords[0], coords[1])

	def isField2(self, i, j):
		return (-self.DIM <= i < self.DIM + 1) and (-self.DIM <= j < self.DIM + 1)

	def isValidField2(self, i, j):
		return self.isField2(i, j) and self.getField2(i, j).isValid()

	def reset(self):
		self.fields = self.makeFields()
		self.selection = Selection()

	def addMarble(self, marble, pos):
		fields[pos[0],pos[1]].addMarble(marble)

	def moveMarble(self, marble, pos):
		pass

	def getFields(self):
		return self.fields

	def isEmptyField(self, x, y):
		return self.getField2(x, y).isEmpty()

	def getField2(self, i, j):
		return self.fields[(i,j)]

	def getField1(self, coords):
		return self.fields[coords]

	def toString(self):
		full = ""
		for y in range(self.DIM, -self.DIM - 1, -1):
			line = " " * (-y + self.DIM)
			for x in range(-self.DIM, self.DIM + 1):
				line += self.fields[(x,y)].toString() + " ";
			full += line + "\n"
		return full

	def isField2(self, i, j):
		return (i, j) in self.fields.keys()

class Cursor:

	def __init__(self, selection):
		self.selection = selection
		self.board = selection.getBoard()
		self.pos = (0, 0)

	def move(self, i, j):
		newPos = (self.pos[0] + i, self.pos[1] + j)
		if self.board.isValidField1(newPos):
			self.pos = newPos

	def string(self):
		return "c"

	def getPos(self):
		return self.pos

	def select(self):
		field = self.board.getField1(self.pos)
		if not field.isEmpty():
			self.selection.select(field.getMarble())

	def toString(self):
		return "Cursor: " + str(self.pos)

class Selection:

	def __init__(self, board):
		self.board = board
		self.selection = []

	def moveBasic(self, x, y):
		for marble in self.selection:
			if not marble.isMoveToFreeSpace(x, y):
				return
		for marble in self.selection:
			marble.move(x, y)
			self.clear()

	def move(self, x, y):
		if not self.isValidSelection():
			return
		else:
			if self.getSize() == 1 or not self.moveIsAlongAxis(x, y):
				self.moveBasic(x, y)
			else:
				self.moveAlongAxis(x, y)

	def moveIsAlongAxis(self, x, y):
		#checks if direction of move is parallel to slope of selection
		#requires selection is valid and has more than one marble
		m1 = self.selection[0].getPos()
		m2 = self.selection[1].getPos()
		ydiff = m2[1] - m1[1]
		xdiff = m2[0] - m1[0]
		for i in range(-2, 3):
			if x * i == xdiff and y * i == ydiff:
				#print("move is along axis")
				return True
		#print("move is not along axis")
		return False

	def moveAlongAxis(self, x, y):
		#requires move is along axis; 2 or 3 marbles
		#first find head
		head = self.selection[0]
		for m in self.selection:
			if (head.getPos()[0] + x, head.getPos()[1] + y) == m.getPos():
				head = m
		#print("head position: ", head.getPos())
		if head.isMoveToFreeSpace(x, y):
			for m in self.selection:
				m.move(x, y)
			self.clear()

	def getSize(self):
		return len(self.selection)

	def clear(self):
		self.selection = []

	def getBoard(self):
		return self.board

	def select(self, marble):
		if marble in self.selection:
			self.removeMarble(marble)
		else:
			self.addMarble(marble)

	def addMarble(self, marble):
		self.selection.append(marble)

	def removeMarble(self, marble):
		self.selection.remove(marble)

	def getMarbles(self):
		return marbles

	def toString(self):
		s = "Selection:\n"
		for m in self.selection:
			s += m.toString() + "\n"
		return s

	def areAdjacent2(self, i1, j1, i2, j2):
		if not (i1 - i2)**2 + (j1 - j2)**2 == 1:
			return False
		return not (i1 - i2 == j1 - j2)

	def isValidSelection2(self):
		i1 = self.selection[0].getPos()[0]
		j1 = self.selection[0].getPos()[1]
		i2 = self.selection[1].getPos()[0]
		j2 = self.selection[1].getPos()[1]
		return self.areAdjacent2(i1, i2, j1, j2)

	def areAdjacent3(self, i1, j1, i2, j2, i3, j3):
		adj12 = self.areAdjacent2(i1, j1, i2, j2)
		adj23 = self.areAdjacent2(i2, j2, i3, j3)
		adj31 = self.areAdjacent2(i3, j3, i1, j1)
		return (adj12 and adj23) or (adj23 and adj31) or (adj31 and adj12)

	def areInSameLine3(self, i1, j1, i2, j2, i3, j3):
		return (i1 == i2 and i2 == i3) or (j1 == j2 == j3) \
			or (i1 - j1 == i2 - j2 and i2 - j2 == i3 - j3)

	def isValidSelection3(self):
		i1 = self.selection[0].getPos()[0]
		j1 = self.selection[0].getPos()[1]
		i2 = self.selection[1].getPos()[0]
		j2 = self.selection[1].getPos()[1]
		i3 = self.selection[2].getPos()[0]
		j3 = self.selection[2].getPos()[1]
		return self.areAdjacent3(i1, j1, i2, j2, i3, j3) and \
			self.areInSameLine3(i1, j1, i2, j2, i3, j3)

	def isValidSelection(self):
		S = self.selection
		l = len(S)
		if l == 0 or l > 3:
			print("Wrong number of fields")
			return False
		elif l == 1:
			return self.board.isValidField1(S[0].getPos())
		elif l == 2:
			return self.isValidSelection2()
		elif l == 3:
			return self.isValidSelection3()
		else:
			return False

class Field:

	def __init__(self, valid, pos):
		self.valid = valid
		self.pos = pos
		self.strings = {True: "o", False: " "}
		self.marbles = []
		self.cursor = False

	def isCursor(self):
		return self.cursor

	def setCursor(self, bool):
		self.cursor = bool

	def isEmpty(self):
		return len(self.marbles) == 0

	def getMarbles(self):
		return self.marbles

	def getMarble(self):
		return self.marbles[-1]

	def addMarble(self, marble):
		self.marbles.append(marble)

	def removeMarble(self, marble):
		self.marbles.remove(marble)

	def isValid(self):
		return self.valid

	def getPos(self):
		return self.pos

	def toString(self):
		if len(self.marbles) > 0:
			return self.marbles[-1].getColor()
		elif self.cursor:
			return "c"
		else:
			return self.strings[self.valid]

class Marble:

	def __init__(self, color, board, startpos):
		self.color = color
		self.field = board.getFields()[startpos]
		self.field.addMarble(self)
		self.board = board

	def move(self, x, y):
		newPos = self.getNewPos(x, y)
		newField = self.board.getFields()[newPos]
		self.field.removeMarble(self)
		self.field = newField
		newField.addMarble(self)

	def isMoveToFreeSpace(self, x, y):
		newPos = self.getNewPos(x, y)
		return self.board.isValidField1(newPos) \
			and self.board.getField1(newPos).isEmpty()

	def getNewPos(self, x, y):
		currentPos = self.field.getPos()
		return (currentPos[0] + x, currentPos[1] + y)

	def getColor(self):
		return self.color

	def getField(self):
		return self.field

	def toString(self):
		return str(self.field.getPos())

	def getPos(self):
		return self.getField().getPos()

class Game:

	def __init__(self):
		self.l = "left"
		self.ru = "rightup"
		self.lu = "leftup"
		self.r = "right"
		self.rd = "rightdown"
		self.ld = "leftdown"
		self.q = "q"
		self.c = "c"
		self.s = "s"
		self.board = Board()
		self.selection = Selection(self.board)
		self.cursor = Cursor(self.selection)
		self.stop = False
		self.help = "put help here"
		self.addMarbles("2players.txt")

	def addMarbles(self, inputFile):
		whole = open(inputFile, "r").read()
		players = whole.split("\n")
		for p in players:
			positions = p.split(" ")
			color = positions.pop(0)
			for pos in positions:
				s = pos.split(",")
				coords = (int(s[0]), int(s[1]))
				Marble(color, self.board, coords)

	def play(self):
		while True:
			print(self.board.toString())
			print(self.cursor.toString())
			print(self.selection.toString())
			self.getInput()
			if self.stop:
				break

	def getInput(self):
		S = str.split(input("Input: "))
		if len(S) == 2:
			if S[0] == self.c:
				t = self.cursor
			elif S[0] == self.s:
				t = self.selection
			else:
				print(self.help)
			inp = S[1]
			if not t is None:
				if inp == self.l:
					t.move(-1, 0)
				elif inp == self.lu:
					t.move(0, 1)
				elif inp == self.ru:
					t.move(1, 1)
				elif inp == self.r:
					t.move(1, 0)
				elif inp == self.ld:
					t.move(0, -1)
				elif inp == self.rd:
					t.move(-1, -1)
				else:
					print(self.help)
		elif len(S) == 1:
			if S[0] == self.q:
				self.stop = True
			elif S[0] == self.s:
				self.cursor.select()
			else:
				print(self.help)
		else:
			print(self.help)

Game().play()
