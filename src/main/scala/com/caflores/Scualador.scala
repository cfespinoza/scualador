package com.caflores

import com.ntic.to_check.{AnalisisExploratorio, Analizador, Contribuyente, Utilidades}
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger
import org.json4s.JsonAST.JObject
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._
import org.slf4j.LoggerFactory

import java.io.{File, FileOutputStream, PrintStream, PrintWriter}
import scala.io.Source.fromFile
import scala.util.{Failure, Success, Try}


object Scualador extends App {
  val config = ConfigFactory.load("application.conf").getConfig("com.caflores")
  val scualador = config.getConfig("scualador")
  val responses = config.getConfig("responses")
  val outputFileImprimeDatos = s"${scualador.getString("results-dir")}/salida_imprime_datos"
  val outputFileImprimeContribuyentes = s"${scualador.getString("results-dir")}/salida_imprime_contribuyentes"
  val logger = Logger(LoggerFactory.getLogger(scualador.getString("app-name")))
  logger.info("=> Logger cargado")
  val dataset = Utilidades.readFile(scualador.getString("dataset-csv"))
  // Ejercicio 1
  val totalDeRegistros: Double = Try({
    AnalisisExploratorio.totalDeRegistros(dataset)
  }) match {
    case Success(response) => if (responses.getInt("totalDeRegistros") == response) 0.5 else 0
    case Failure(_) => 0
  }
  // Ejercicio 2
  val calculaEdadMedia: Double = Try({
    AnalisisExploratorio.calculaEdadMedia(dataset)
  }) match {
    case Success(response) =>
      val toValidResponse = parseDistribucion(response)
      val validResponse = parseDistribucion(responses.getDouble("calculaEdadMedia"))
      if (toValidResponse == validResponse)
        0.5
      else 0
    case Failure(_) => 0
  }
  // Ejercicio 3
  val calculaEdadMediaNoZeros: Double = Try({
    AnalisisExploratorio.calculaEdadMediaNoZeros(dataset)
  }) match {
    case Success(response) =>
      val toValidResponse = parseDistribucion(response)
      val validResponse = parseDistribucion(responses.getDouble("calculaEdadMediaNoZeros"))
      if (toValidResponse == validResponse)
        1
      else 0
    case Failure(_) => 0
  }
  // Ejercicio 4
  val paisesOrigenUnicos: Double = Try({
    AnalisisExploratorio.paisesOrigenUnicos(dataset)
  }) match {
    case Success(response) =>
      val validResponse = responses.getStringList("paisesOrigenUnicos")
      if (response.length == validResponse.size() && response.forall(validResponse.contains(_)))
        1
      else 0
    case Failure(_) => 0
  }
  // Ejercicio 5
  val distribucionPorGeneros: Double = Try({
    AnalisisExploratorio.distribucionPorGeneros(dataset)
  }) match {
    case Success(response) =>
      val (hombres, mujeres) = (parseDistribucion(response._1), parseDistribucion(response._2))
      val (hombresResponse, mujeresResponse) = (
        parseDistribucion(responses.getDoubleList("distribucionPorGeneros").get(0)),
        parseDistribucion(responses.getDoubleList("distribucionPorGeneros").get(1))
      )
      if (hombres == hombresResponse && mujeres == mujeresResponse)
        1
      else 0
    case Failure(_) => 0
  }
  // Ejercicio 6
  val trabajoMejorRemunerado: Double = Try({
    AnalisisExploratorio.trabajoMejorRemunerado(dataset)
  }) match {
    case Success(response) =>
      if (response == responses.getString("trabajoMejorRemunerado"))
        2
      else 0
    case Failure(_) => 0
  }
  // Ejercicio 7
  val aniosEstudiosMedio: Double = Try({
    AnalisisExploratorio.aniosEstudiosMedio(dataset)
  }) match {
    case Success(response) =>
      if (response == responses.getDouble("aniosEstudiosMedio"))
        2
      else 0
    case Failure(_) => 0
  }
  // Ejercicio 8
  val applyContribuyente: Double = Try({
    Contribuyente()
  }) match {
    case Success(c: Contribuyente) =>
      val desconocido = "desconocido"
      val negativo = -1
      if (c.income == desconocido
        && c.age == negativo
        && c.workclass == desconocido
        && c.education == desconocido
        && c.educationNum == negativo
        && c.maritalStatus == desconocido
        && c.occupation == desconocido
        && c.relationship == desconocido
        && c.race == desconocido
        && c.sex == desconocido
        && c.capitalGain == negativo
        && c.capitalLoss == negativo
        && c.hoursPerWeek == negativo
        && c.nativeCountry == desconocido)
        1
      else 0
    case Failure(_) => 0
  }
  // Ejercicio 9
  val imprimeDatos: Double = Try({
    Console.withOut(new PrintStream(new FileOutputStream(outputFileImprimeDatos))) {
      Contribuyente.imprimeDatos(Contribuyente())
    }
  }) match {
    case Success(_) => if (fromFile(outputFileImprimeDatos).getLines().forall(_ == responses.getString("imprimeDatos")))
      1
    else 0
    case Failure(exception) => 0
  }
  // Ejercicio 10
  val analizador: Double = AnalisisExploratorio match {
    case x: Analizador => 0.5
    case x: App => 0
    case _ => 0
  }
  // Ejercicio 11
  val imprimeContribuyentes: Double = Try({
    Console.withOut(new PrintStream(new FileOutputStream(outputFileImprimeContribuyentes))) {
      AnalisisExploratorio.imprimeContribuyentes(Seq(Contribuyente(), Contribuyente()))
    }
  }) match {
    case Success(_) => if (fromFile(outputFileImprimeContribuyentes).getLines().forall(_ == responses.getString("imprimeDatos")))
      1.5
    else 0
    case Failure(_) => 0
  }
  val nota = totalDeRegistros + calculaEdadMedia + calculaEdadMediaNoZeros + paisesOrigenUnicos + distribucionPorGeneros + trabajoMejorRemunerado + aniosEstudiosMedio + applyContribuyente + imprimeDatos + analizador + imprimeContribuyentes

  def parseDistribucion(d: Double): Double =
    BigDecimal(if (d < 1) d else d / 100).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble

  logger.debug(s"totalDeRegistros: ${totalDeRegistros}")
  logger.debug(s"calculaEdadMedia: ${calculaEdadMedia}")
  logger.debug(s"calculaEdadMediaNoZeros: ${calculaEdadMediaNoZeros}")
  logger.debug(s"paisesOrigenUnicos: ${paisesOrigenUnicos}")
  logger.debug(s"distribucionPorGeneros: ${distribucionPorGeneros}")
  logger.debug(s"trabajoMejorRemunerado: ${trabajoMejorRemunerado}")
  logger.debug(s"aniosEstudiosMedio: ${aniosEstudiosMedio}")
  logger.debug(s"applyContribuyente: ${applyContribuyente}")
  logger.debug(s"imprimeDatos: ${imprimeDatos}")
  logger.debug(s"analizador: ${analizador}")
  logger.debug(s"imprimeContribuyentes: ${imprimeContribuyentes}")
  val notas: JObject = (
    ("totalDeRegistros" -> totalDeRegistros) ~
    ("calculaEdadMedia" -> calculaEdadMedia) ~
    ("calculaEdadMediaNoZeros" -> calculaEdadMediaNoZeros) ~
    ("paisesOrigenUnicos" -> paisesOrigenUnicos) ~
    ("distribucionPorGeneros" -> distribucionPorGeneros) ~
    ("trabajoMejorRemunerado" -> trabajoMejorRemunerado) ~
    ("aniosEstudiosMedio" -> aniosEstudiosMedio) ~
    ("applyContribuyente" -> applyContribuyente) ~
    ("imprimeDatos" -> imprimeDatos) ~
    ("analizador" -> analizador) ~
    ("imprimeContribuyentes" -> imprimeContribuyentes) ~
    ("nota" -> nota)
  )
  val notasPW = new PrintWriter(new File(s"${scualador.getString("notas-dir")}/${scualador.getString("alumno")}"))
  notasPW.write(compact(render(notas)))
  notasPW.close()
  logger.info(s"Nota Final: ${nota}")
}
