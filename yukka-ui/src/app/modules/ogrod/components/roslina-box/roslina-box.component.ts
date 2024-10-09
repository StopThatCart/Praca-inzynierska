import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import {CdkDrag, CdkDragEnd, CdkDragHandle, CdkDragMove, DragRef, Point} from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-roslina-box',
  standalone: true,
  imports: [CdkDrag, CdkDragHandle],
  templateUrl: './roslina-box.component.html',
  styleUrl: './roslina-box.component.css'
})
export class RoslinaBoxComponent implements OnInit {

  @Input() dragPosition = {x: 0, y: 0};
  @Output() dragMoved = new EventEmitter<CdkDragMove>();

  ngOnInit(): void {
    console.log('RoslinaBoxComponent');
    //this.dragPosition = {x: 0, y: 0};
    console.log('Initial position - x: ' + this.dragPosition.x + ' y: ' + this.dragPosition.y);
  }

  onDrag(event: CdkDragMove) {
   // event.source.getFreeDragPosition();
    //const { x, y } = event.source.getFreeDragPosition();
    //this.dragging.emit({ x, y });
    this.dragMoved.emit(event);
    //console.log('x: ' + event.source.getFreeDragPosition().x + ' y: ' + event.source.getFreeDragPosition().y);
    //this.dragPosition = event.source.getFreeDragPosition();
  }

  onDragEnded(event: CdkDragEnd): void {
    const dragPosition = event.source.getFreeDragPosition();


    this.dragPosition = dragPosition;
    console.log('x: ' + this.dragPosition.x + ' y: ' + this.dragPosition.y);
  }
}
