# compilerDesign

This repo contains the full code to Caleb, Derek, and Jimmy's Compiler and Interpreter.

The Scanner class parses through an inputted text file, checking the Syntax of the file and returns Tokens. Each Token has a Type determined by a Finite State Machine. Tokens are used by the Parser class to check the Semantics of the file and by the Interpreter class to produce the output of the file once it is compiled.

The Parser class determines whether the Semantics of the inputted text file is correct by retrieving Tokens from the Scanner and checking whether the Token is expected according to a LR1 Grammar for the Pascal-esque language. The Parser will flag errors in the file if it encounters an unexpected Token while Parsing and will additionally create an output file of Quads for the program.

The Interpreter class utilizes the Quads outputted by the Parser and runs the program by executing the necessary instructions for each Quad.

## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
