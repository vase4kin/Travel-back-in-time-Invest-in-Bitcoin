# Retro Compose visual contract

Commit `0602649` is the authoritative source for the Android presentation. The images in
`screenshots/` are supporting references only: they predate the final generic share action and must
not be used to restore the removed Facebook and Twitter controls.

## Foundation

- Palette: black `#000000`, white `#FFFFFF`, red `#F44336`, surface `#212121`, gray border
  `#979797`, light border `#F6F1F1`, and date-picker header `#412121`.
- Background: pure black with `ic_splash` center-cropped at 20% opacity.
- Type: VT323 for titles, descriptions, actions, and amount input; Press Start 2P at 10sp for
  dashboard labels; Digital Dream Fat Narrow for red dashboard values.
- Primary actions: 64dp tall, square black container, 1dp white outline, white uppercase VT323 text,
  and no visible elevation.
- Screen content uses 16dp outer padding. Consecutive actions use an 8dp vertical gap.

## Input and dialogs

- The input title is at the top, the quotation occupies the flexible center, and the three actions
  remain at the bottom. Selected dates use `dd MMM yyyy`; money uses US currency formatting.
- The amount sheet uses the dark surface and 16dp top corners. Presets are `$1`, `$10.00`, and
  `$100.00`; selecting one confirms immediately. Empty, sub-dollar, and first-time million-dollar
  warnings match the legacy strings. A second confirmation accepts a million-dollar value.
- The date picker is limited by `TimeTravelConstraints`, opens at the maximum date, and uses the
  monochrome scheme with the legacy dark-red header accent.

## Result, loading, and error

- Results use two square dashboard panels. The destination panel separates month, year, invested
  digits, and dollar sign. The present-time panel separates profit digits and dollar sign. Digits
  use `%,.2f` without an embedded currency symbol.
- A successful request plays `car_animation.gif` once at 0.8x speed before the result appears.
- Result actions are Share with friends and Start over. The older marketing screenshots' social
  network buttons are intentionally not restored.
- Error title and copy remain centered while Retry is anchored to the bottom with 16dp margins.
- Splash-to-input uses the legacy 800ms fade with 200ms delay. Forward routes use the equivalent
  800ms horizontal slide with 200ms delay.

## Reference states

Visual regression coverage must include the initial and completed input states, loading/disabled
input, amount sheet, date picker, result, loading animation, and error screen at a representative
phone size and a compact phone size.
