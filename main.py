import re

#Page 110 of Elements of Computing Systems
#Encapsulates access to the input code. Reads an assembly language com- mand,
# parses it, and provides convenient access to the command’s components (fields
# and symbols). In addition, removes all white space and comments.
class Parser:
    def __init__(self, file_name):
        self.commands = []
        self.file_name = file_name 

    def parse(self):
        with open(self.file_name, 'r') as asmfile:
            for line in asmfile:
                ln = line.strip()
                ln = re.sub(r'\/\/.*', '', ln)
                if ln != '':
                    self.commands.append(ln)

    def clean_commands(self):
        cleaned_commands = []
        for command in self.commands:
            if command.startswith('@'):
                cleaned_commands.append(command)
            else:
                if '=' in command or ';' in command:
                    cleaned_commands.append(command)
        return cleaned_commands

#Page 112
#SymbolTable: Keeps a correspondence between symbolic labels and numeric addresses.
#make sure you copy all of the values carefully
class SymbolTable:
    def __init__(self):
        self.symbols = {'SP': 0, 'LCL': 1, 'ARG': 2, 'THIS': 3, 'THAT': 4,
                        'SCREEN': 16384, 'KBD': 24576}
        for i in range(16):
            self.symbols['R' + str(i)] = i

    def find_exact_symbols(self, commands):
        lineno = 0
        for command in commands:
            symbol = re.findall(r'\(.+\)', command)
            if symbol:
                symbol = symbol[0][1:-1]
                if symbol not in self.symbols:
                    self.symbols[symbol] = lineno
                    print(symbol)
                    print(lineno)
            else:
                lineno += 1

    def find_variables(self, commands):
        variableno = 16
        for command in commands:
            symbol = re.findall(r'@[a-zA-Z]+.*', command)
            if symbol and symbol[0][1:] not in self.symbols:
                self.symbols[symbol[0][1:]] = variableno
                variableno += 1

#Pagee 111 of textbook
#Code: Translates Hack assembly language mnemonics into binary codes.
#copy carefully from book
class Code:
    def __init__(self, symbol_table):
        self.dest = {'': '000', 'M': '001', 'D': '010', 'MD': '011',
                     'A': '100', 'AM': '101', 'AD': '110', 'AMD': '111'}
        self.jump = {'': '000', 'JGT': '001', 'JEQ': '010', 'JGE': '011',
                     'JLT': '100', 'JNE': '101', 'JLE': '110', 'JMP': '111'}
        self.comp = {'0': '0101010', '1': '0111111', '-1': '0111010', 'D': '0001100',
                     'A': '0110000', 'M': '1110000', '!D': '0001101', '!A': '0110001',
                     '!M': '1110001', '-D': '0001111', '-A': '0110011', '-M': '1110011',
                     'D+1':'0011111', 'A+1': '0110111', 'M+1': '1110111', 'D-1': '0001110',
                     'A-1': '0110010', 'M-1': '1110010', 'D+A': '0000010', 'D+M': '1000010',
                     'D-A': '0010011', 'D-M':'1010011', 'A-D': '0000111', 'M-D': '1000111',
                     'D&A': '0000000', 'D&M': '1000000', 'D|A': '0010101', 'D|M': '1010101'}
        self.symbol_table = symbol_table

    def generate_code(self, command):
        if command[0] == '@':
            return self._address_to_binary(command[1:])
        else:
            if '=' in command or ';' in command:
                dest, comp, jump = '', '', ''
                parts = command.split('=')
                if len(parts) == 2:
                    dest, command = parts
                parts = command.split(';')
                if len(parts) == 2:
                    comp, jump = parts
                else:
                    comp = command
                comp_code = self.comp.get(comp, '0000000')
                dest_code = self.dest.get(dest, '000')
                jump_code = self.jump.get(jump, '000')
                return '111' + comp_code + dest_code + jump_code
            else:
                return '111' + self.comp.get('0', '0000000') + self.dest.get('', '000') + self.jump.get('', '000')

    def _address_to_binary(self, address):
        if address.isdigit():
            return format(int(address), '016b')
        elif address.startswith('@R'):
            return format(int(address[2:]),'016b')
        elif address in self.symbol_table.symbols:
            return format(self.symbol_table.symbols[address], '016b')
        else:
            self.symbol_table.symbols[address] = self.symbol_table.next_variable_address
            self.symbol_table.next_variable_address += 1
            return format(self.symbol_table.symbols[address], '016b')

class Main:
    def run(self):
        file_name = input('file:')
        parser = Parser(file_name)
        parser.parse()
        cleaned_commands = parser.clean_commands()
        print(cleaned_commands)
        symbol_table = SymbolTable()
        symbol_table.find_exact_symbols(parser.commands)
        symbol_table.find_variables(cleaned_commands)
        code = Code(symbol_table)
        with open(file_name + '1.hack', 'w') as hackfile:
            for command in cleaned_commands:
                binary_code = code.generate_code(command)
                if binary_code is not None:
                    hackfile.write(binary_code + '\n')
                    print(command + " = " + binary_code)

if __name__ == "__main__":
    main = Main()
    main.run()