import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DzialkaTilePickerComponent } from './dzialka-tile-picker.component';

describe('DzialkaTilePickerComponent', () => {
  let component: DzialkaTilePickerComponent;
  let fixture: ComponentFixture<DzialkaTilePickerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DzialkaTilePickerComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DzialkaTilePickerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
