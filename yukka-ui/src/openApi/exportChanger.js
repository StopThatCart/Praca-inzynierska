const fs = require('fs');
const filePath = './src/app/services/models.ts';

console.log("Zamiana 'export {' na 'export type {' w pliku models.ts");
fs.readFile(filePath, 'utf8', (err, data) => {
  if (err) {
    console.error(err);
    process.exit(1);
  }
  const updatedData = data.split('\n')
    .map(line => line.replace('export {', 'export type {')).join('\n');

  fs.writeFile(filePath, updatedData, 'utf8', (err) => {
    if (err) {
      console.error(err);
      process.exit(1);
    }
    console.log("Zaktualizowano plik.");
  });
});
