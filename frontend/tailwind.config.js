/** @type {import('tailwindcss').Config} */
module.exports = {
  darkMode: 'class',
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        primary: '#64A7C5',
        secondary: '#1C4B68',
        accent: '#F39C12',
        fondo: '#FFFFFF',
        texto: '#333333',
        'fondo-dark': '#121A21',
        'tarjeta-dark': '#1E2833',
        'texto-dark': '#F0F0F0'
      }
    },
  },
  plugins: [],
}

