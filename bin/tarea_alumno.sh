#!/bin/bash

# ./bin/tarea_alumno.sh ~/Workspace/ntic/ntic_scala/tareas/masterdatascienceclase1_2020_2021 RequeniLizondoAndrea
zip_files_dir=${1:?"Carpeta de tareas no establecido"}
filename=${2:?"Nombre de alumn@ no establecido"}
decompressed_files="${zip_files_dir}/descomprimidos"
cwd=$(pwd)

student_dir="${decompressed_files}/${filename}"

cd "${student_dir}"
analisis_exploratorio_path=$(find -name AnalisisExploratorio.scala)
analizdor_path=$(find -name Analizador.scala)
contribuyente_path=$(find -name Contribuyente.scala)
cd ${cwd}


sed '1 s/^/package com\.ntic\.to_check\n/' "${student_dir}/${analisis_exploratorio_path}" > "${cwd}/src/main/scala/com/ntic/to_check/AnalisisExploratorio.scala"
sed '1 s/^/package com\.ntic\.to_check\n/' "${student_dir}/${analizdor_path}" > "${cwd}/src/main/scala/com/ntic/to_check/Analizador.scala"
sed '1 s/^/package com\.ntic\.to_check\n/' "${student_dir}/${contribuyente_path}" > "${cwd}/src/main/scala/com/ntic/to_check/Contribuyente.scala"



