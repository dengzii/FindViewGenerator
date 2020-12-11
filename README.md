# FindViewGenerator

> Free your hands, make coding easy.

**An AndroidStudio plugin use for generate findViewById code automatic.**

**Support both of Java and Kotlin code generation.**

## Install

- install from release jar

  - Navigation -> File -> Settings -> Plugins -> On the top side, click 'gear' icon -> install plugin form disk -> choose the jar you download

- install from IntelliJ Plugin Repository

   - Navigation -> File -> Settings -> Plugins -> search 'Generate FindViewById'

## Usage

- Press 'Alt + Insert', in generate popup window, the first item is 'Generate FindViewById' 

- Press 'Ctrl + Alt + 1'

## Build 

Kotlin dependency `<depends>org.jetbrains.kotlin</depends>` must import kotlin plugin jars as project dependency first, 
import dependency from `Project Structure > Module > Dependencies`, choose kotlin plugin your IDEA installation path > plugin > kotlin > lib. 

## Screenshots

![generate mapping dialog](https://raw.githubusercontent.com/MrDenua/FindViewGenerator/master/screen_shot/generate_dialog.png)

![generate code](https://raw.githubusercontent.com/MrDenua/FindViewGenerator/master/screen_shot/generated_code.png)
