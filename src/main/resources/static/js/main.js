
//Получение индекса элемента в коллекции
function getIndex(list, id) {
    for (var i = 0; i < list.length; i++ ) {
        if (list[i].id === id) {
            return i;
        }
    }
    return -1;
}

//Обращение к серверу
var messageApi = Vue.resource('/message{/id}');

//Создаем форму
Vue.component('messages-form', {
    props: ['messages', 'messageAttr'],
    data: function () {
        return {
            text: '',
            id: ''
        }
    },
    // каждый раз при изменении messageAttr наблюдатель будет оповещен
    watch: {
        messageAttr: function(newVal, oldVal) {
            this.text = newVal.text;
            this.id = newVal.id;
        }
    },
    template:
        '<div>' +
            '<input type="text" placeholder="some" v-model="text" />' + //V-model указывает переменную text
            '<input type="button" value="save" @click="save" />' +
        '</div>',
    methods: {
        save: function() {
            var message = { text: this.text };

            if (this.id) {
                messageApi.update({id: this.id}, message).then(result =>
                    result.json().then(data => {
                        var index = getIndex(this.messages, data.id);
                        this.messages.splice(index, 1, data);
                        this.text = '';
                        this.id = '';
            })
                )
            } else {
                messageApi.save({}, message).then(result =>
                    result.json().then(data => {
                        this.messages.push(data);
                        this.text = '';
                    })
                )
            }
        }
    }
});

//Конкретный элемент списка
Vue.component('messages-row', {
    props: ['message', 'editMethod', 'messages'],    //даем компоненту понять что он будет принимать какие то данные из message
    template:
        '<div>' +
            '<i>({{ message.id }})</i> {{ message.text }}' +
            '<span style="position: absolute; right: 0;">' +
                '<input type="Button" value="Edit" @click="edit" />' +
                '<input type="Button" value="X" @click="del" />' +
        '</span>' +
        '</div>',
    methods: {
        edit: function () {
            this.editMethod(this.message)
        },
        del: function() {
            messageApi.remove({id: this.message.id}).then(result => {
                if (result.ok) {
                    this.messages.splice(this.messages.indexOf(this.message), 1)
                }
            })
        }
    }
});

//Вывод всего списка
Vue.component('messages-list', {
    props: ['messages'],    //даем компоненту понять что он будет принимать какие то данные из message
    data: function() {
        return {
            message: null
        }
    },
    template:
        '<div style="position: relative; width: 300px;">' +
            '<messages-form :messages="messages" :messageAttr="message" />' +
            '<messages-row v-for="message in messages" :key="message.id" :message="message" ' +
            ':editMethod="editMethod" :messages="messages" />' +
        '</div>',
    created: function() {
        messageApi.get().then(result =>
            result.json().then(data =>
                data.forEach(message => this.messages.push(message))
            )
        )
    },
    methods: {
        editMethod: function(message) {
            this.message = message;
        }
    }
});

//Передача данных в список и его вывод
var app = new Vue({
    el: '#app', //app - id html элемента
    template: '<messages-list :messages="messages"/>',
    data: {
        messages: []
    }
});