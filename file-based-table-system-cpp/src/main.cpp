#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <string>
using namespace std;

vector<string> columnHeaders;
vector<vector<string>> tableData;

void clearInput() {
    cin.clear();
    cin.ignore(10000, '\n');
}

int getIntInput(string message) {
    int number;

    while (true) {
        cout << message;

        if (cin >> number) {
            clearInput();
            return number;
        }

        cout << "Invalid input! Please enter a number." << endl;
        clearInput();
    }
}

string getLineInput(string message) {
    string value;
    cout << message;
    getline(cin, value);
    return value;
}

void writeCsvToTxt(const string &fileName);
void displayContent(const string &fileName);

bool fileCreate(const string &fileName) {
    ifstream existingFile(fileName + ".txt");

    if (existingFile.is_open()) {
        cout << "Error: File \"" << fileName << ".txt\" already exists!" << endl;
        existingFile.close();
        return false;
    }

    ofstream file(fileName + ".txt");

    if (!file.is_open()) {
        cout << "Error opening file for writing!" << endl;
        return false;
    }

    cout << "File \"" << fileName << ".txt\" has been successfully created!" << endl;
    file.close();

    return true;
}

void tableCreate(const string &fileName) {
    columnHeaders.clear();
    tableData.clear();

    string tableName = getLineInput("Please enter the table name: ");

    int numColumns = getIntInput("How many columns do you want in your table?: ");

    while (numColumns <= 0) {
        cout << "Number of columns must be more than 0." << endl;
        numColumns = getIntInput("How many columns do you want in your table?: ");
    }

    cout << "Please enter the short column names." << endl;

    for (int i = 0; i < numColumns; i++) {
        string shortColName = getLineInput("Column " + to_string(i + 1) + ": ");
        string fullColName = tableName + "_" + shortColName;
        columnHeaders.push_back(fullColName);
    }

    int numRows = getIntInput("How many rows of data do you want to add initially?: ");

    while (numRows < 0) {
        cout << "Number of rows cannot be negative." << endl;
        numRows = getIntInput("How many rows of data do you want to add initially?: ");
    }

    for (int i = 0; i < numRows; i++) {
        vector<string> rowData;
        cout << "\nEntering data for row " << i + 1 << ":" << endl;

        for (int j = 0; j < numColumns; j++) {
            string value = getLineInput(columnHeaders[j] + ": ");
            rowData.push_back(value);
        }

        tableData.push_back(rowData);
    }

    writeCsvToTxt(fileName);
    cout << "Table created and saved in " << fileName << ".txt!" << endl;
    displayContent(fileName);
}

bool fileOpen(const string &fileName) {
    ifstream infile(fileName + ".mdb");

    if (!infile.is_open()) {
        cout << "Error: Cannot open " << fileName << ".mdb for reading!" << endl;
        return false;
    }

    columnHeaders.clear();
    tableData.clear();

    string line;
    bool isFirstLine = true;

    while (getline(infile, line)) {
        if (line.empty()) {
            continue;
        }

        vector<string> tokens;
        string token;
        stringstream ss(line);

        while (getline(ss, token, ',')) {
            tokens.push_back(token);
        }

        if (isFirstLine) {
            columnHeaders = tokens;
            isFirstLine = false;
        } else {
            tableData.push_back(tokens);
        }
    }

    infile.close();

    if (columnHeaders.empty()) {
        cout << "Error: The file is empty or invalid!" << endl;
        return false;
    }

    writeCsvToTxt(fileName);

    cout << "Successfully loaded data from " << fileName << ".mdb" << endl;
    displayContent(fileName);

    return true;
}

void writeCsvToTxt(const string &fileName) {
    ofstream file(fileName + ".txt");

    if (!file.is_open()) {
        cout << "Error opening file!" << endl;
        return;
    }

    for (int i = 0; i < columnHeaders.size(); i++) {
        file << columnHeaders[i];

        if (i < columnHeaders.size() - 1) {
            file << ",";
        }
    }

    file << endl;

    for (int i = 0; i < tableData.size(); i++) {
        for (int j = 0; j < tableData[i].size(); j++) {
            file << tableData[i][j];

            if (j < tableData[i].size() - 1) {
                file << ",";
            }
        }

        file << endl;
    }

    file.close();
}

void displayContent(const string &fileName) {
    cout << "\nTable Content for " << fileName << ".txt:" << endl;

    if (columnHeaders.empty()) {
        cout << "No table data available." << endl;
        return;
    }

    for (int i = 0; i < columnHeaders.size(); i++) {
        cout << columnHeaders[i];

        if (i < columnHeaders.size() - 1) {
            cout << ",";
        }
    }

    cout << endl;

    if (tableData.empty()) {
        cout << "No rows available." << endl;
        return;
    }

    for (int i = 0; i < tableData.size(); i++) {
        cout << i + 1 << ". ";

        for (int j = 0; j < tableData[i].size(); j++) {
            cout << tableData[i][j];

            if (j < tableData[i].size() - 1) {
                cout << ",";
            }
        }

        cout << endl;
    }
}

void fileDelete(const string &fileName) {
    displayContent(fileName);

    if (tableData.empty()) {
        cout << "Error: No row to delete!" << endl;
        return;
    }

    int rowNumber = getIntInput("Enter row number to delete: ");

    if (rowNumber < 1 || rowNumber > tableData.size()) {
        cout << "Error: Invalid row number!" << endl;
        return;
    }

    tableData.erase(tableData.begin() + rowNumber - 1);
    writeCsvToTxt(fileName);

    cout << "Row deleted successfully!" << endl;
    displayContent(fileName);
}

void fileUpdate(const string &fileName) {
    displayContent(fileName);

    if (tableData.empty()) {
        cout << "Error: No row to update!" << endl;
        return;
    }

    int rowNumber = getIntInput("Enter row number to update: ");

    if (rowNumber < 1 || rowNumber > tableData.size()) {
        cout << "Error: Invalid row number!" << endl;
        return;
    }

    cout << "\nSelected row:" << endl;

    for (int i = 0; i < columnHeaders.size(); i++) {
        cout << i + 1 << ". " << columnHeaders[i]
             << " = " << tableData[rowNumber - 1][i] << endl;
    }

    int columnNumber = getIntInput("Enter column number to update: ");

    if (columnNumber < 1 || columnNumber > columnHeaders.size()) {
        cout << "Error: Invalid column number!" << endl;
        return;
    }

    string newValue = getLineInput("Enter new value: ");

    tableData[rowNumber - 1][columnNumber - 1] = newValue;
    writeCsvToTxt(fileName);

    cout << "Record updated successfully!" << endl;
    displayContent(fileName);
}

void fileCount() {
    cout << "The number of rows in the table: " << tableData.size() << endl;
}

void insertNewRow(const string &fileName) {
    displayContent(fileName);

    if (columnHeaders.empty()) {
        cout << "Error: No table structure exists!" << endl;
        return;
    }

    vector<string> newRow;

    cout << "\nEnter new row data:" << endl;

    for (int i = 0; i < columnHeaders.size(); i++) {
        string value = getLineInput(columnHeaders[i] + ": ");
        newRow.push_back(value);
    }

    tableData.push_back(newRow);
    writeCsvToTxt(fileName);

    cout << "New row inserted successfully!" << endl;
    displayContent(fileName);
}

void secondOption(const string &fileName) {
    while (true) {
        cout << "\n===== Secondary Menu =====" << endl;
        cout << "1. Delete a specific row" << endl;
        cout << "2. Update a specific field" << endl;
        cout << "3. Count the number of rows" << endl;
        cout << "4. Insert a new row" << endl;
        cout << "5. Exit program" << endl;
        cout << "6. Back to main menu" << endl;

        int option = getIntInput("Enter your choice: ");

        switch (option) {
            case 1:
                fileDelete(fileName);
                break;

            case 2:
                fileUpdate(fileName);
                break;

            case 3:
                fileCount();
                break;

            case 4:
                insertNewRow(fileName);
                break;

            case 5:
                cout << "Exiting program..." << endl;
                exit(0);

            case 6:
                return;

            default:
                cout << "Invalid choice!" << endl;
        }
    }
}

int main() {
    while (true) {
        cout << "\n===== Main Menu =====" << endl;
        cout << "1. Create a file" << endl;
        cout << "2. Open existing file" << endl;
        cout << "3. Exit" << endl;

        int mode = getIntInput("Enter your choice: ");

        string fileName;

        switch (mode) {
            case 1:
                fileName = getLineInput("Please enter the file name without .txt: ");

                if (fileCreate(fileName)) {
                    tableCreate(fileName);
                    secondOption(fileName);
                }

                break;

            case 2:
                fileName = getLineInput("Please enter the file name without .mdb: ");

                if (fileOpen(fileName)) {
                    secondOption(fileName);
                }

                break;

            case 3:
                cout << "Exiting program..." << endl;
                return 0;

            default:
                cout << "Invalid choice, please try again!" << endl;
        }
    }

    return 0;
}