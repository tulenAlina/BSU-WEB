const menuData = [
    {
      name: 'Автомобильная страховка', submenu: [
        {
          name: 'Страхование от угона', submenu: [
            { name: 'Защита от угона автомобил', url: '../Lab2/table.html' },
            { name: 'Возмещение ущерба при угоне', url: '../Lab2/table.html' }
          ]
        },
        { name: 'Обязательная гражданская ответственность', url: '../Lab2/table.html' },
        {
          name: 'Каско (страховка от повреждений и ущерба автомобиля)', submenu: [
            { name: 'Страхование от аварийных повреждений', url: '../Lab2/table.html' },
            { name: 'Страхование от угона', url: '../Lab2/table.html' },
            { name: 'Страхование от стихийных бедствий', url: '../Lab2/table.html' },
            { name: 'Страхование от грабежа и вандализма', url: '../Lab2/table.html' }
          ]
        }
      ]
    },
    { name: 'Медицинское страхование', url: '../Lab2/table.html' },
    {
      name: 'Имущественное страхование', submenu: [
        { name: 'Страхование недвижимости', url: '../Lab2/table.html' },
        { name: 'Страхование имущества', url: '../Lab2/table.html' }
      ]
    }
  ];
  
  const showMenu = (items, parent) => {
    const listElement = document.createElement('ul');
    items.forEach((item) => {
      const itemElement = makeMenuItem(item);
      listElement.appendChild(itemElement);
    });
    parent.appendChild(listElement);
  }
  
  const makeMenuItem = (item) => {
    const listItemElem = document.createElement('li');
    if (item.submenu) {
      const submenuToggle = document.createElement('i');
      submenuToggle.classList.add('fas', 'fa-caret-down');
      const submenuToggleWrapper = document.createElement('span');
      submenuToggleWrapper.appendChild(submenuToggle);
      listItemElem.append(item.name, submenuToggleWrapper);
      listItemElem.onclick = (event) => {
        if (event.target !== event.currentTarget) return;
        if (hideMenu(listItemElem)) {
          listItemElem.classList.remove('opened');
          return;
        }
        showMenu(item.submenu, listItemElem);
        listItemElem.classList.add('opened');
      };
      listItemElem.classList.add('has-submenu');
    }
    if (item.url) {
      const anchor = makeAnchor(item.name, item.url);
      listItemElem.appendChild(anchor);
    }
  
    listItemElem.classList.add('menu-item');
  
    return listItemElem;
  }
  
  const makeAnchor = (name, url) => {
    const listItemAnchor = document.createElement('a');
    listItemAnchor.textContent = name;
    listItemAnchor.href = url;
    return listItemAnchor;
  }
  
  const hideMenu = (parent) => {
    const listElement = parent.querySelector('ul');
    if (listElement) {
      listElement.remove();
      return true;
    }
    return false;
  }
  
  const menuContainer = document.getElementById('tree-menu');
  
  showMenu(menuData, menuContainer);