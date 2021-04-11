#!/bin/bash

zip_files_dir=$1
curso=$2
grupo=$3
decompressed_files="${zip_files_dir}/descomprimidos"
rm -rf "${decompressed_files}"
mkdir "${decompressed_files}"

for f in $(ls ${zip_files_dir}); do
  filename=$(echo $f | cut -d "." -f 1)  # nombre_alumno
  extension=$(echo $f | cut -d "." -f 2)
  printf "filename: ${filename}"
  printf "\n"
  printf "extension: ${extension}"
  printf "\n"
  if [ "${f}" != "descomprimidos" ]; then
    student_dir="${decompressed_files}/${filename}"
    mkdir "${student_dir}"
    case ${extension} in
    zip) unzip "${zip_files_dir}/${f}" -d "${student_dir}" ;;
    rar) unrar e -r -o- "${zip_files_dir}/${f}" "${student_dir}" ;;
    esac



  fi

done
