# Welcome!
This GitHub repository contains the source code for a Java Enigma Simulator, written by Brennan Stride, and documented with the help of Martin Hernandez-Gamez and Hunter Craig. To use the Simulator, download or clone the project (see [below](#frequently-asked-questions)).

## Description Of Enigma_Sim 
Enigma_Sim is a Java simulator for the Wehermacht Enigma, a German encryption device used primarily in World War II. The simulator is built to run with the terminal on a linux machine. It takes the input of a formatted .txt file (see [Formatting Settings.txt](#formatting-settings)) and outputs the encrypted or decrypted string, as given by another input file, or user input in the terminal.

## How To Use
Download or clone the project. The class files are included, so compilation is unnecessary. Open a shell, or use a java file execution program to run the file titled "Enigma". The simulator requires a command line argument specifying the file from which the program will read instructions. There is a second, optional, command for a file from which to read input. If the command line is blank, the program will fail.  
    **EXAMPLE EXECUTION (with file input):**   
    `$ java Enigma Settings.txt SampleText.txt`  
    `VIGSXSEWOU`   
    **EXAMPLE EXECUTION (without file input):**   
    `$ java Enigma Settings.txt`  
    `Hello World`  
    `VIGSXSEWOU`  

## Frequently Asked Questions
#### What is the enigma?
   The [enigma](https://en.wikipedia.org/wiki/Enigma_machine) was a machine used by Germany during the second world war to encrypt their  communications. This encryption changed daily and was considered impossible to be broken consistently by human codebreakers. 
#### Why would I use this?
   Use this program if:
   * You are interested in how the enigma functions and want to see it in action
   * You have an interest in cryptography
   * You have an interest in WWII history
#### Can I copy this for academic purposes?
   Absolutely! This project is licensed to be free to reproduce under the MIT license.
#### What programming language is this written in?
   This project is written in entirely in java.
#### Will I need a java IDE to compile?
   The class files are provided, so there is no need to compile.
   
## File Manifest
+ README.md                     :: this file
+ Enigma_Simulator              :: directory holding project files
  + `Enigma$Machine.class`      :: machine object class
  + `Enigma$Rotor.class`        :: rotor object class
  + `Enigma.class`              :: holds main method and static machine object
  + Enigma.java                 :: the java program itself
  + Settings.txt                :: sample settings
  + Settings2.txt               :: sample settings 2
  + SampleText.txt              :: contains "Hello World"

## Licensing
This project is licensed under the MIT license. For more detail, read the [LICENSE](https://github.com/stridebn/Enigma_Sim/blob/master/LICENSE) file.

## Collaboration
If you wish to collaborate, this github repository is the place to do it. Contact <stridebn@appstate.edu> with any questions about specifics.
Some specific ideas for improvement:
+ GUI implementation
+ Executable (remove need for command line)
+ Write program for inputting settings (outputs correctly formatted text file)

## Support
Contact <hernandezgamezm@appstate.edu> with any technical support questions. (but *please* check the FAQ and instructions above first)
