export default class HexPosition {
    Q: number = 0;
    R: number = 0;
};

export const positionMinus = (a: HexPosition, b: HexPosition) => ({
   Q: a.Q - b.Q,
   R: a.R - b.R
});


export const positionsEqual = (a: HexPosition, b: HexPosition) => a.Q === b.Q && a.R === b.R;