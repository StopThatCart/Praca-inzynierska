import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoslinaBoxComponent } from './roslina-box.component';

describe('RoslinaBoxComponent', () => {
  let component: RoslinaBoxComponent;
  let fixture: ComponentFixture<RoslinaBoxComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RoslinaBoxComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RoslinaBoxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
