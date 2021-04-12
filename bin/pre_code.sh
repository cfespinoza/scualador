#!/bin/bash

# ./bin/pre_code.sh ~/Workspace/ntic/ntic_scala/tareas/masterdatascienceclase1_2020_2021 2020-2021 masterdatascienceclase1

zip_files_dir=$1
curso=${2:-"nocurso"}
grupo=${3:-"nogrupo"}
decompressed_files="${zip_files_dir}/descomprimidos"
rm -rf "${decompressed_files}"
mkdir "${decompressed_files}"

cwd=$(pwd)
export NOTAS_DIR="${cwd}/notas/${curso}/${grupo}"
rm -rf "${NOTAS_DIR}"
mkdir -p "${NOTAS_DIR}"

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

    # Copiado trabajo
    cp "${student_dir}/src/main/scala/AnalisisExploratorio.scala" "${cwd}/src/main/scala/ntic/to_check/"
    cp "${student_dir}/src/main/scala/Analizador.scala" "${cwd}/src/main/scala/ntic/to_check/"
    cp "${student_dir}/src/main/scala/Contribuyente.scala" "${cwd}/src/main/scala/ntic/to_check/"

    export ALUMNO=${filename}
    export CURSO=${curso}
    export GRUPO=${grupo}

    sbt clean update
    sbt package
    sbt assembly
    java -classpath "${cwd}/target/scala-2.11/${ALUMNO}-1.0.jar" com.caflores.Scualador
  fi

done
