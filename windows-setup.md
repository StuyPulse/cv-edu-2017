## Setting up on Windows

First, these things need to be installed:

- The Java Development Kit (JDK)
  ([here](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html))
  needs to be up to date. I recommend skipping this step for now. Follow the
  rest of the steps below, and update the JDK only if there is an error at the
  end, because the install is slow and you may already have an updated version.

- Git should be installed already for most of you. If not, it can be installed
  [here](http://git-scm-org).

- Apache Ant. You can run `ant -version` in Git Bash to see if it's already
  installed (version 1.9.x should be fine). If not, use [this
  link](http://mirrors.koehn.com/apache//ant/binaries/apache-ant-1.9.7-bin.zip)
  to download it, and then extract it to wherever you like (e.g., just `C:\`).
  Then add `path-to-ant\bin` to your PATH, by searching for Environment
  Variables from Start menu, editing the PATH variable, and adding a New entry.
  This allows you to run `ant` from Git Bash.

- Clone the repo [https://github.com/Team694/cv-edu-2017](), or if you've
  already cloned it, run `git pull`.

After this, install OpenCV with [this
installer](https://sourceforge.net/projects/opencvlibrary/files/opencv-win/3.0.0/)
on SourceForge. When it asks where to extract the files, enter
`C:\...\cv-edu-2017\lib` (filling in the `...` with the path to cv-edu-2017).

Finally, run `ant compile` (in Git Bash) in the `cv-edu-2017` directory, and
then `ant run`. It should compile and run successfully, with a window showing
up like the one we saw at the meeting.

If `ant run` fails...

- Did you skip the step of updating the JDK? Update it now.

- You updated the JDK, and it's still not working? Run `java -version` and
  `javac -version`. If they're different, or if they're not at version
  1.8.0_101, the JDK binaries are not in PATH. Add `C:\...\jdk1.8.0_101\bin` to
  PATH (filling in ... with full path).

If `ant compile` fails, make sure `cv-edu-2017/lib/opencv-3.0.0` (the OpenCV
installation) exists.

Ask Wilson, James, or Helen if it still doesn't work.
