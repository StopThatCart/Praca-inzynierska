import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MoveRoslinaToOtherDzialkaComponent } from './move-roslina-to-other-dzialka.component';

describe('MoveRoslinaToOtherDzialkaComponent', () => {
  let component: MoveRoslinaToOtherDzialkaComponent;
  let fixture: ComponentFixture<MoveRoslinaToOtherDzialkaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MoveRoslinaToOtherDzialkaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MoveRoslinaToOtherDzialkaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
