package com.example.myapplication.utils

import android.content.Context
import com.example.myapplication.QC.data.model.HistoryItem
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ExcelUtils {
    fun createExcelFile(context: Context, historyItems: List<HistoryItem>): File? {
        val workbook: Workbook = XSSFWorkbook()
        val sheet: Sheet = workbook.createSheet("Historique")

        // Créer l'en-tête
        val headerRow: Row = sheet.createRow(0)
        val headers = arrayOf("ID", "Line", "Team", "DefectsCount")
        var cellNum = 0
        headers.forEach { header ->
            val cell = headerRow.createCell(cellNum++)
            cell.setCellValue(header)
            val style: CellStyle = workbook.createCellStyle()
            cell.cellStyle = style
        }

        // Ajouter les données
        historyItems.forEachIndexed { index, item ->
            val row: Row = sheet.createRow(index + 1)
          //  row.createCell(0).setCellValue(item.id)
            row.createCell(0).setCellValue(item.line)
            row.createCell(1).setCellValue(item.team)
            row.createCell(2).setCellValue(item.defectsCount.toDouble())
        }

        // Enregistrer le fichier Excel
        val file = File(context.getExternalFilesDir(null), "historique_${System.currentTimeMillis()}.xlsx")
        return try {
            FileOutputStream(file).use { outputStream ->
                workbook.write(outputStream)
            }
            file
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            workbook.close()
        }
    }
}
