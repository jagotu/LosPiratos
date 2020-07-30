export default function getContrastColor(backgroundColor: string): string {
    // Stack overflow whatever https://stackoverflow.com/questions/105034/how-to-create-guid-uuid
    if (backgroundColor.indexOf('#') === 0) {
        backgroundColor = backgroundColor.slice(1);
    }
    // convert 3-digit backgroundColor to 6-digits.
    if (backgroundColor.length === 3) {
        backgroundColor = backgroundColor[0] + backgroundColor[0] + backgroundColor[1] + backgroundColor[1] + backgroundColor[2] + backgroundColor[2];
    }
    if (backgroundColor.length !== 6) {
        throw new Error('Invalid backgroundColor color.');
    }
    const r = parseInt(backgroundColor.slice(0, 2), 16),
        g = parseInt(backgroundColor.slice(2, 4), 16),
        b = parseInt(backgroundColor.slice(4, 6), 16);

    return (r * 0.299 + g * 0.587 + b * 0.114) > 140
        ? '#000000'
        : '#FFFFFF';

}