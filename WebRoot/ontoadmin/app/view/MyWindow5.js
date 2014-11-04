/*
 * File: app/view/MyWindow5.js
 *
 * This file was generated by Sencha Architect version 2.2.2.
 * http://www.sencha.com/products/architect/
 *
 * This file requires use of the Ext JS 4.2.x library, under independent license.
 * License of Sencha Architect does not include license for Ext JS 4.2.x. For more
 * details see http://www.sencha.com/license or contact license@sencha.com.
 *
 * This file will be auto-generated each and everytime you save your project.
 *
 * Do NOT hand edit this file.
 */

Ext.define('MyApp.view.MyWindow5', {
    extend: 'Ext.window.Window',

    height: 250,
    id: 'AddRelOfCon1',
    width: 400,
    layout: {
        type: 'absolute'
    },
    title: 'My Window',

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'combobox',
                    x: 50,
                    y: 50,
                    id: 'insRel',
                    fieldLabel: '实例关系',
                    store: 'AttTermForClass'
                },
                {
                    xtype: 'combobox',
                    x: 50,
                    y: 110,
                    id: 'insVal',
                    fieldLabel: '实例值',
                    store: 'InsTermStore'
                },
                {
                    xtype: 'button',
                    x: 170,
                    y: 170,
                    text: '确定添加',
                    listeners: {
                        click: {
                            fn: me.onButtonClick,
                            scope: me
                        }
                    }
                }
            ]
        });

        me.callParent(arguments);
    },

    onButtonClick: function(button, e, eOpts) {
        var node=Ext.getCmp('InsTreeView').getSelectionModel().getSelection();
        var n=node[0];
        //n.appendChild({text:'请输入概念名',leaf:true});
        var oldterm=n.get('text');
        //Ext.getCmp('updateConTerm').setValue(oldterm);

        alert('功能开发中………………，需要加入的是关系的继承和关系的添加');



    }

});