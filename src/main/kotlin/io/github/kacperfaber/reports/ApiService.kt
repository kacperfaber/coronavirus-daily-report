package io.github.kacperfaber.reports

import io.github.kacperfaber.http.HttpService
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ApiService(
    val covidReportJsonReader: JsonReader<CovidReport>,
    var httpService: HttpService,
    var dateWriter: IDateTimeWriter,
    var reportsJsonReader: JsonReader<Array<CovidReport>>,
    var vaccinationReportsDeserializer: JsonReader<Array<VaccinationReport>>
) {
    fun getCovidReport(): CovidReport? { // TODO: Replace with 'DailyReport' class
        val response = httpService.get("https://koronawirus-api.herokuapp.com/api/covid-vaccinations-tests/daily")
        if (response.isSuccessful) {
            return covidReportJsonReader.read(response.body!!.string())
        }
        return null
    }

    fun getVaccinationReports(from: LocalDate, to: LocalDate): Array<VaccinationReport> {
        val content = httpService.getContent(
            "https://koronawirus-api.herokuapp.com/api/vaccinations/from/${dateWriter.writeDate(from)}/to/${
                dateWriter.writeDate(
                    to
                )
            }"
        )
        return vaccinationReportsDeserializer.read(content)
    }

    fun getAllVaccinationReports(): Array<VaccinationReport> {
        return getVaccinationReports(LocalDate.of(2019, 1, 1), LocalDate.now())
    }

    fun getCovidReports(from: LocalDate, to: LocalDate): Array<CovidReport> {
        val content = httpService.getContent(
            "https://koronawirus-api.herokuapp.com/api/covid-vaccinations-tests/from/${dateWriter.writeDate(from)}/to/${
                dateWriter.writeDate(
                    to
                )
            }"
        )
        return reportsJsonReader.read(content)
    }

    fun getAllCovidReports(): Array<CovidReport> {
        val content = httpService.getContent(
            "https://koronawirus-api.herokuapp.com/api/covid19/from/${
                dateWriter.writeDate(
                    LocalDate.of(2019, 12, 1)
                )
            }/to/${dateWriter.writeDate(LocalDate.now())}"
        )
        return reportsJsonReader.read(content)
    }

    fun getCovidReport(date: LocalDate): CovidReport? {
        val r = httpService.get(
            "https://koronawirus-api.herokuapp.com/api/covid-vaccinations-tests/from/${dateWriter.writeDate(date)}/to/${
                dateWriter.writeDate(date.plusDays(1))
            }"
        )
        if (r.isSuccessful) {
            val content = r.body!!.string()
            val reports = reportsJsonReader.read(content)
            return reports.find { x -> x.reportDate == date }
        }
        return null
    }
}