import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DzialkaCardComponent } from './dzialka-card.component';

describe('DzialkaCardComponent', () => {
  let component: DzialkaCardComponent;
  let fixture: ComponentFixture<DzialkaCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DzialkaCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DzialkaCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
