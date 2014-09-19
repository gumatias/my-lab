#!/bin/sh

#########################################################
# usage
# sh undo_convention.sh [file_or_directory]
#
# example
# execute in given file only
# sh undo_convention.sh ~/path/to/project/MyClass.java
#
# execute in all files in directory (non-recursive).
# sh undo_convention.sh ~/path/to/project/package-name
#########################################################

path=$1
# mFoo = pFoo; -> this.foo = foo;
regex_for_constructor="%s/\([ \t]*\)[^this.]*m\([A-Z]\{1\}\)\([a-zA-Z0-9]\+\)[ ]*=[ ]*p\([A-Z]\{1\}\)\([a-zA-Z0-9]\+\);/\1this.\l\2\3 = \l\4\5;/g"
# mFoo = foo; -> this.foo = foo;
regex_for_setters="%s/\([\t]\{1,\}\|{ \)[^this.]*m\([A-Z]\{1\}\)\([a-zA-Z0-9]\+\)[ ]*=[ ]*\([a-z]\{2,\}[a-zA-Z0-9]*\);/\1this.\l\2\3 = \4;/g"
# oFoo -> foo, pFoo -> foo, mFoo -> foo
regex_for_all_fields="%s/\([^a-zA-Z]\)[opm]\{1\}\([A-Z]\{1\}\)\([a-zA-Z0-9]\+\)/\1\l\2\3/g"

if [ -z $path ]
then
  echo "Please provide the file or directory as an argument"
  exit 0
fi

undo_convention() {
  vi -c "$regex_for_constructor" -c wq $1
  vi -c "$regex_for_setters" -c wq $1
  vi -c "$regex_for_all_fields" -c wq $1
}

if [ -d $path ]
then
  for f in `ls $path`
  do
    if [ ! -d "$path/$f" ]
    then
      undo_convention "$path/$f"
    fi
  done
elif [ -e $path ]
then
  undo_convention $path
fi

echo "\nDone!\nPlease watch out for compilation errors or warnings in your files in case I messed up\n"

