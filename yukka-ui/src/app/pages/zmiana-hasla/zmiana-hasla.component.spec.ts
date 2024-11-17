import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ZmianaHaslaComponent } from './zmiana-hasla.component';

describe('ZmianaHaslaComponent', () => {
  let component: ZmianaHaslaComponent;
  let fixture: ComponentFixture<ZmianaHaslaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ZmianaHaslaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ZmianaHaslaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
