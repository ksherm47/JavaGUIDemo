# Development Guidelines

## Branching
- Do not commit directly to master!
- When beginning work on a new feature, you should create a personal branch off of master.  
- When you are done, you should create a pull request to merge your changes into master.  
- Once your pull request is approved by the other developers, your changes will be merged in the master branch.  
  
## JDK
- Make sure you have a version of JDK installed.  I'm using JDK 10.

## IntelliJ Configuration
- Open TeamMark folder in IntelliJ  
  

- Ctrl + Shift + Alt + S
    - Project -> Set project compiler output to TeamMark/out
    - Project -> Project SDK -> 10
    - SDKs -> Click + -> Add ...\Program Files\Java\jdk-10... folder
    - SDKs -> Click + -> Add ...\Program Files\Java\jre...\lib\ext\jfxrt.jar
  
- In Project tab, right click TeamMark/src -> Mark Directory As -> Sources Root
    - Same thing, just mark TeamMark/out as Excluded
  
- Run -> Edit Configurations -> click + -> Application
    - Choose sample.Main as Main Class, click Ok
  
- Now you should be able to Run -> Run 'Main'