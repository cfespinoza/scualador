#!/bin/bash
START="$(date +%s)"
source ~/Workspace/venvs/cflores/bin/activate
# ./bin/pre_code.sh ~/Workspace/ntic/ntic_scala/tareas/masterdatascienceclase1_2020_2021 2020-2021 masterdatascienceclase1

zip_files_dir=$1
curso=${2:-"nocurso"}
grupo=${3:-"nogrupo"}
decompressed_files="${zip_files_dir}/descomprimidos"
rm -rf "${decompressed_files}"
mkdir "${decompressed_files}"

cwd=$(pwd)
export NOTAS_DIR="${cwd}/notas/${curso}/${grupo}"
export LOGS_DIR="${cwd}/logs/${curso}/${grupo}"
rm -rf "${NOTAS_DIR}"
rm -rf "${LOGS_DIR}"
mkdir -p "${NOTAS_DIR}"
mkdir -p "${LOGS_DIR}"

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
    cd "${student_dir}"
    analisis_exploratorio_path=$(find -name AnalisisExploratorio.scala)
    analizdor_path=$(find -name Analizador.scala)
    contribuyente_path=$(find -name Contribuyente.scala)
    cd ${cwd}

    sed '1 s/^/package com\.ntic\.to_check\n/' "${student_dir}/${analisis_exploratorio_path}" > "${cwd}/src/main/scala/com/ntic/to_check/AnalisisExploratorio.scala"
    sed '1 s/^/package com\.ntic\.to_check\n/' "${student_dir}/${analizdor_path}" > "${cwd}/src/main/scala/com/ntic/to_check/Analizador.scala"
    sed '1 s/^/package com\.ntic\.to_check\n/' "${student_dir}/${contribuyente_path}" > "${cwd}/src/main/scala/com/ntic/to_check/Contribuyente.scala"


    export ALUMNO=${filename}
    export CURSO=${curso}
    export GRUPO=${grupo}

    sbt clean update
    sbt package > "${LOGS_DIR}/${ALUMNO}.log"
    sbt assembly >> "${LOGS_DIR}/${ALUMNO}.log"
    java -classpath "${cwd}/target/scala-2.11/${ALUMNO}-1.0.jar" com.caflores.Scualador >> "${LOGS_DIR}/${ALUMNO}.log"
  fi
done
python "${cwd}/python/score_summarizer.py"
END="$(date +%s)"
DURATION=$[ ${END} - ${START} ]
echo "Ejecucion en ${DURATION} segundos"