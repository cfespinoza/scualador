package com.ntic.resolved


object AnalisisExploratorio extends Analizador {

  val dataset: Seq[Contribuyente] = Utilidades.readFile(fichero = "src/adult.data.clean.csv")

  // Implementa la función
  // ejercicio-1:
  // Número total de registros en el dataset.
  def totalDeRegistros(c: Seq[Contribuyente]): Int = c.size

  // Implementa la función
  // ejercicio-2:
  // Calcular la media de edad de todos los contribuyentes
  def calculaEdadMedia(c: Seq[Contribuyente]): Double = c.map(_.age).sum.toDouble / c.size.toDouble


  // Implementa la función
  // ejercicio-3:
  // Calcular la media de edad de todos los contribuyentes sin contar aquellos cuya edad sea 0
  def calculaEdadMediaNoZeros(c: Seq[Contribuyente]): Double = {
    val noZeros = c.filter(_.age != 0).map(_.age)
    noZeros.sum.toDouble / noZeros.size.toDouble
  }


  // Implementa la función
  // ejercicio-4:
  // Descubrir de cuántos países distintos provienen los contribuyentes
  def paisesOrigenUnicos(c: Seq[Contribuyente]): Seq[String] = c.map(_.nativeCountry).toSet.toSeq // c.map(_.nativeCountry).distinct

  // Implementa la función
  // ejercicio-5:
  // De todos los contribuyentes, ¿cómo se distribuye por género?. Devuelve el porcentaje de hombres
  // y el de mujeres en ese orde, (porcentajeDeHombres, porcentajeDeMujeres)
  def distribucionPorGeneros(c: Seq[Contribuyente]): (Double, Double) = {
    // val male = c.filter(_.sex.equalsIgnoreCase("male"))
    val male = c.filter(_.sex == "Male")
    val malePerc = male.size.toDouble / c.size.toDouble
    (malePerc, 1 - malePerc)

  }

  // Implementa la función
  // ejercicio-6:
  // Cuál es el tipo de trabajo (workclass) cuyos ingresos son mayoritariamente superiores a ">50K
  def trabajoMejorRemunerado(c: Seq[Contribuyente]): String = {
    c.groupBy(_.workclass).map(kv => (kv._1, kv._2.count(_.income == ">50K").toDouble / kv._2.size.toDouble)).maxBy(kv => kv._2)._1
    /*
    val workclasses = c.map(_.workclass).distinct // Obtenemos listado con todas las workclasses
    var a:Double = -1.0
    var b:String = null
    for (i <- workclasses) {
      val wc_size:Double = c.count(x => x.workclass == i) // c.filter(x => x.workclass == i).size //Número total de instancias para cada workclass
      val plus50_size:Double = c.filter(x => x.workclass == i).count(x => x.income == ">50K") //Número de instancias con income >50k para cada workclass
      val plus50_prop = plus50_size/wc_size //Proporción de personas que ganan más de 50k en cada workclass
      if (plus50_prop > a) { //Lo que haremos con el if, será quedarnos con aquella workclass cuya proporción entre
        a = plus50_prop //el número de contribuyentes que cobran más de 50k, y el número total de contribuyentes para esa clase
        b = i //sea la mayor entre todas las workclasses.
      }
    }
    b*/
  }

  // Implementa la función
  // ejercicio-7:
  // Cuál es la media de años de educación (education-num) de aquellos contribuyentes cuyo país de origen no es
  // United-States
  def aniosEstudiosMedio(c: Seq[Contribuyente]): Double = {
    val noUnitedStates = c.filter(_.nativeCountry != "United-States").map(_.educationNum)
    noUnitedStates.sum.toDouble / noUnitedStates.size.toDouble
  }


  //  println(s" -> Dataset tiene un total de registros: ${totalDeRegistros(c = dataset)}")
  //  println(s" -> En el dataset, los contribuyentes tienen una edad media: ${calculaEdadMedia(c = dataset)}")
  //  println(s" -> En el dataset, los contribuyentes tienen una edad media (sin contar aquellos con age = 0): ${calculaEdadMediaNoZeros(c = dataset)}")
  //  println(s" -> Los contribuyentes proviende de distintos países como: ${paisesOrigenUnicos(c = dataset).foreach(println)}")
  //  println(s" -> Los contribuyentes se distribuyen en (hombres - mujeres): ${distribucionPorGeneros(c = dataset)}")
  //  println(s" -> El tipo de trabajo mejor remunerado en el dataset es: ${trabajoMejorRemunerado(c = dataset)}")
  //  println(s" -> La media de años de estudio de los contribuyenes de origen distinto a United States es: ${aniosEstudiosMedio(c = dataset)}")

  // ejercicio-11
  // llama a la función impimeContribuyentes
  imprimeContribuyentes(c = dataset)

}