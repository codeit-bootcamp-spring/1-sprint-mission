interface ThemeColors {
  brand: {
    primary: string;
    hover: string;
  };
  background: {
    primary: string;
    secondary: string;
    tertiary: string;
    input: string;
    hover: string;
  };
  text: {
    primary: string;
    secondary: string;
    muted: string;
  };
  status: {
    online: string;
    idle: string;
    dnd: string;
    offline: string;
    error: string;
    [key: string]: string;
  };
  border: {
    primary: string;
  };
}

interface Theme {
  colors: ThemeColors;
}

export const theme: Theme = {
  colors: {
    brand: {
      primary: '#5865F2',
      hover: '#4752C4',
    },
    background: {
      primary: '#1a1a1a',
      secondary: '#2a2a2a',
      tertiary: '#333333',
      input: '#40444B',
      hover: 'rgba(255, 255, 255, 0.1)',
    },
    text: {
      primary: '#ffffff',
      secondary: '#cccccc',
      muted: '#999999',
    },
    status: {
      online: '#43b581',
      idle: '#faa61a',
      dnd: '#f04747',
      offline: '#747f8d',
      error: '#ED4245',
    },
    border: {
      primary: '#404040',
    },
  },
} 