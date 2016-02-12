# run this command, and copy and paste the numbers into an excel spreadsheet.  
# Use the sum tool to add up the number of lines
find . -name "*.java" -exec wc -l \{\} \; | cut -d. -f1
