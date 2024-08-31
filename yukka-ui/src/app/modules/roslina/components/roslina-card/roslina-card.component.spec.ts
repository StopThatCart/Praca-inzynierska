import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoslinaCardComponent } from './roslina-card.component';

describe('RoslinaCardComponent', () => {
  let component: RoslinaCardComponent;
  let fixture: ComponentFixture<RoslinaCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RoslinaCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RoslinaCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
