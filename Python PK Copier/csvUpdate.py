import csv

def append_column(input_file, output_file, column_index):
    # Open the input file in read mode and output file in append mode
    with open(input_file, 'r') as input_csv, open(output_file, 'a') as output_csv:
        # Create a CSV reader and writer
        reader = csv.reader(input_csv)
        writer = csv.writer(output_csv)

        # Iterate through the rows in the input file
        for row in reader:
            # Append the value at the specified column index to the output file
            writer.writerow([row[column_index]])

# Example usage
append_column('C:\\Users\\sanaa\\Computer Science\\Common-SideEffects\\Python PK Copier\\SideEffectTable.csv', 'C:\\Users\\sanaa\\Computer Science\\Common-SideEffects\\Python PK Copier\\AllMeddra.csv', 1)