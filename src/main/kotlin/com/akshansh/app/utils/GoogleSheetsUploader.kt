package com.akshansh.app.utils

import com.akshansh.app.models.SheetsData
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.*
import java.io.File

const val CREDENTIAL_FILE_PATH = "./playground-5c299-bc174ed590ca.json"
const val GOOGLE_SHEETS_ENDPOINT = "https://www.googleapis.com/auth/spreadsheets"

const val APP_NAME = "Event Details"

const val COLUMN_EVENT_NAME = "Event Name"
const val COLUMN_PROTO_DETAILS = "Proto Details"
const val COLUMN_COMMENTS = "Comments"

class GoogleSheetsUploader {
    fun pushToGoogleSheets(rows: List<SheetsData>, spreadsheetId: String, sheetName: String) {
        val credentialsFile = File(CREDENTIAL_FILE_PATH)

        val credential = GoogleCredential.fromStream(credentialsFile.inputStream())
            .createScoped(listOf(GOOGLE_SHEETS_ENDPOINT))

        val sheetsService = Sheets.Builder(
            NetHttpTransport(),
            JacksonFactory.getDefaultInstance(),
            credential
        )
            .setApplicationName(APP_NAME)
            .build()

        val values = mutableListOf<List<Any>>()
        values.add(listOf(COLUMN_EVENT_NAME, COLUMN_PROTO_DETAILS, COLUMN_COMMENTS))
        for (row in rows) {
            values.add(listOf(row.eventName, row.protoDetails, row.comments))
        }

        val valueRange = ValueRange()
            .setValues(values)

        sheetsService.spreadsheets().values()
            .update(spreadsheetId, "$sheetName!A1", valueRange)
            .setValueInputOption("RAW")
            .execute()

        val requests = listOf(
            Request().setRepeatCell(
                RepeatCellRequest().apply {
                    range = GridRange().apply {
                        sheetId = sheetId
                        startRowIndex = 0
                        endRowIndex = 1
                        startColumnIndex = 0
                        endColumnIndex = 3
                    }
                    cell = CellData().apply {
                        userEnteredFormat = CellFormat().apply {
                            backgroundColor = Color().apply {
                                red = 0.2f
                                green = 0.6f
                                blue = 0.86f
                            }
                            textFormat = TextFormat().apply {
                                bold = true
                                foregroundColor = Color().apply {
                                    red = 1f
                                    green = 1f
                                    blue = 1f
                                }
                            }
                        }
                    }
                    fields = "userEnteredFormat(backgroundColor,textFormat)"
                }
            ),

            Request().setUpdateDimensionProperties(
                UpdateDimensionPropertiesRequest().apply {
                    range = DimensionRange().apply {
                        sheetId = sheetId
                        dimension = "COLUMNS"
                        startIndex = 0
                        endIndex = 3
                    }
                    properties = DimensionProperties().apply {
                        pixelSize = 500
                    }
                    fields = "pixelSize"
                }
            ),

            Request().setUpdateDimensionProperties(
                UpdateDimensionPropertiesRequest().apply {
                    range = DimensionRange().apply {
                        sheetId = sheetId
                        dimension = "ROWS"
                        startIndex = 0
                        endIndex = 1
                    }
                    properties = DimensionProperties().apply {
                        pixelSize = 60
                    }
                    fields = "pixelSize"
                }
            )
        )

        val batchRequest = BatchUpdateSpreadsheetRequest().apply {
            this.requests = requests
        }

        sheetsService.spreadsheets().batchUpdate(spreadsheetId, batchRequest).execute()

        println("Data pushed to Google Sheet")
    }
}