import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadRoslinaObrazPageComponent } from './upload-roslina-obraz-page.component';

describe('UploadRoslinaObrazPageComponent', () => {
  let component: UploadRoslinaObrazPageComponent;
  let fixture: ComponentFixture<UploadRoslinaObrazPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UploadRoslinaObrazPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UploadRoslinaObrazPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
