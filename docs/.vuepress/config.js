const { description } = require('../../package')

module.exports = {
  title: 'HomeAssistantMC',
  base: '/HomeAssistantMC/',
  description: description,
  head: [
    ['meta', { name: 'theme-color', content: '#03a9f4' }],
    ['meta', { name: 'apple-mobile-web-app-capable', content: 'yes' }],
    ['meta', { name: 'apple-mobile-web-app-status-bar-style', content: 'black' }]
  ],
  themeConfig: {
    repo: '',
    editLinks: false,
    docsDir: '',
    editLinkText: '',
    lastUpdated: false,
    nav: [
      {
        text: 'Guide',
        link: '/guide/',
      },
      {
        text: 'Github',
        link: 'https://github.com/Codestian/HomeAssistantMC'
      },
      {
        text: 'CurseForge',
        link: 'https://www.curseforge.com/members/thecodestian/projects'
      }
    ],
    sidebar: {
      '/guide/': [
        {
          title: 'Guide',
          collapsable: false,
          children: [
            '',
            'installation',
            'configuration',
            ['state-block', 'State Block'],
            ['service-block', 'Service Block'],
            'building-automations',
            'faq'
          ]
        }
      ],
    }
  }
}
