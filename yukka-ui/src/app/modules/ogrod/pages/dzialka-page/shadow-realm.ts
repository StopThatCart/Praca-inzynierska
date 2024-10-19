/*
Tutaj trafia nieużywany kod, którego nie na razie nie usuwam bo może się przydać w przyszłości



  onCanvasClick(event: MouseEvent): void {
    const canvas = this.canvas.nativeElement;
    const rect = canvas.getBoundingClientRect();
    const x = (event.clientX - rect.left) / this.scale;
    const y = (event.clientY - rect.top) / this.scale;
    const col = Math.floor(x / this.tileSize);
    const row = Math.floor(y / this.tileSize);
    console.log(`Tile clicked: (${col}, ${row})`);
    const tile = this.findTile(col, row);
    if(tile) {
      this.onTileClick(tile);
    }
  }


 // Jak wywali dla tileów protokół 431 to tym można to objejść


  //const imageBlob = this.base64ToBlob(zasadzonaRoslina.obraz ?? '', this.images.grass);
 // const imageUrl = URL.createObjectURL(imageBlob);
  // I dla tile ustawiasz obraz taki:
  //  image: imageUrl || this.images.grass,



    base64ToBlob(base64: string, contentType: string): Blob {
      const byteCharacters = atob(base64);
      const byteNumbers = new Array(byteCharacters.length);
      for (let i = 0; i < byteCharacters.length; i++) {
        byteNumbers[i] = byteCharacters.charCodeAt(i);
      }
      const byteArray = new Uint8Array(byteNumbers);
      return new Blob([byteArray], { type: contentType });
    }


          // Zaktualizuj roślinę w this.dzialka
      //const index = this.dzialka.zasadzoneRosliny?.findIndex(r => r.roslina?.id === this.selectedRoslina?.roslina?.id);
      //if (this.dzialka.zasadzoneRosliny && index !== undefined && index !== -1) {
      //    console.log('Zaktualizowano pozycję rośliny w this.dzialka');
        //  this.dzialka.zasadzoneRosliny[index].x = tile.x;
        //  this.dzialka.zasadzoneRosliny[index].y = tile.y;
        //  this.dzialka.zasadzoneRosliny[index].pozycje = this.selectedRoslina.pozycje;
     // }
*/
