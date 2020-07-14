export default interface HexPosition {
    Q: number;
    R: number;
};

export const positionsEqual = (a: HexPosition, b: HexPosition) => a.Q === b.Q && a.R === b.R;